package nugoh.core;

import nugoh.sdk.Action;
import nugoh.sdk.ActionNode;
import nugoh.sdk.PojoFactory;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 13, 2009
 * Time: 2:28:02 PM
 */
public class ActionOSGI implements Action, ServiceListener {
    private final Logger logger = LoggerFactory.getLogger(ActionOSGI.class);

    private final BundleContext bundleContext;
    private final ActionNode actionNode;
    private final long timeout;
    private ActionProxy actionProxy = null;
    private final Object notifier = new Object();
    private final String service;

    public ActionOSGI(BundleContext bundleContext, ActionNode actionNode, long timeout) throws InvalidSyntaxException {
        this.bundleContext = bundleContext;
        this.actionNode = actionNode;
        this.timeout = timeout;

        service = actionNode.getAttributes().get("service");
        if (service == null) {
            String msg = "service attribute is mandatory";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
        registerListener();
        setCurrentService();
    }

    synchronized private void setCurrentService() throws InvalidSyntaxException {
        ServiceReference[] sRef = bundleContext.getAllServiceReferences(null, getFilterString());
        if (sRef != null && sRef.length > 0) {
            Object pojoAction = bundleContext.getService(sRef[0]);
            actionProxy = createActionProxy(pojoAction);
        }
    }

    private String getFilterString() {

        String serviceSantized = service.replaceAll("[^a-zA-Z0-9.]", "_");
        String filterString = "(service=" + serviceSantized + ")";
        logger.debug("filter: " + filterString);

        return filterString;
    }

    private ActionProxy createActionProxy(Object pojoAction) {
        ActionProxy myActionProxy;
        try {
            Object newPojoInstance;
            if(pojoAction instanceof PojoFactory){
                newPojoInstance = ((PojoFactory)pojoAction).newInstance();
            }
            else if(pojoAction instanceof Cloneable){
                Method clone = pojoAction.getClass().getMethod("clone");
                newPojoInstance = clone.invoke(pojoAction);
            }
            else{
                newPojoInstance = pojoAction.getClass().newInstance();
            }
            myActionProxy = new ActionProxy(newPojoInstance , actionNode);
        } catch (Exception e) {
            String msg = "cannot create pojo instance";
            logger.error(msg, e);
            throw new IllegalStateException(msg);
        }
        return myActionProxy;
    }

    private void registerListener() throws InvalidSyntaxException {
        String filterString = getFilterString();
        logger.debug("register listener");
        bundleContext.addServiceListener(this, filterString);
    }

    synchronized private ActionProxy getActionProxy() throws InterruptedException {
        synchronized (notifier) {
            if (actionProxy == null) {
                notifier.wait(timeout);
            }
        }
        return actionProxy;
    }

    @Override
    public void run(Map<String, Object> context) throws Exception {
        ActionProxy action = actionProxy;
        if (action == null) {
            action = getActionProxy();
            if (action == null) {
                throw new IllegalStateException("service not found");
            }
        }
        if (!action.isInitialized()) {
            action.init();
        }
        action.run(context);
    }

    @Override
    synchronized public void serviceChanged(ServiceEvent serviceEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("New Action service event");
        }

        ServiceReference sRef = serviceEvent.getServiceReference();

        if (serviceEvent.getType() == ServiceEvent.REGISTERED || serviceEvent.getType() == ServiceEvent.MODIFIED) {
            actionProxy = createActionProxy(bundleContext.getService(sRef));
            synchronized (notifier) {
                notifier.notifyAll();
            }
        } else if (serviceEvent.getType() == ServiceEvent.UNREGISTERING) {
            actionProxy = null;
        }
    }
}
