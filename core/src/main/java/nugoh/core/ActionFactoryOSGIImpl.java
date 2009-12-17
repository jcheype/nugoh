package nugoh.core;

import nugoh.sdk.Action;
import nugoh.sdk.ActionFactory;
import nugoh.sdk.ActionInit;
import nugoh.sdk.ActionNode;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 5:22:02 PM
 */
public class ActionFactoryOSGIImpl implements ActionFactory {
    private final Logger logger = LoggerFactory.getLogger(ActionFactoryOSGIImpl.class);
    private final Map<String, ServiceRegistration> registrations = new HashMap();
    private BundleContext bundleContext;

    public ActionFactoryOSGIImpl(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public Action createAction(ActionNode actionNode, long timeout, boolean autoRegister) throws Exception{
        Action action = new ActionOSGI(bundleContext,actionNode, timeout);
        String id = actionNode.getAttributes().get("id");
        if(autoRegister && id != null){
            registerAction(action, id);
        }
        return action;
    }

    @Override
    synchronized public void registerAction(Action action, String actionId) throws InvalidSyntaxException {
        actionId = actionId.replaceAll("[^a-zA-Z0-9.]", "_");

        Properties props = new Properties();
        props.put("type", "Action");
        props.put("name", actionId);
        props.put("MainApplication", "nugoh");

        ServiceRegistration previous = registrations.get(actionId);
        if(previous != null){
            logger.info("unregistering service: " + actionId);
            previous.unregister();
        }
        logger.info("registering: " + actionId);
        ServiceRegistration sReg = bundleContext.registerService(Action.class.getName(), action, props);
        registrations.put(actionId,sReg);
    }

    @Override
    synchronized public void unregisterAction(String id) throws Exception {
        ServiceRegistration sReg = registrations.remove(id);
        if(sReg != null){
            sReg.unregister();
        }
    }

    @Override
    public Action retrieveAction(String actionId){
        if(actionId == null){
            String msg = "service attribute is mandatory";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
        Action action = null;
        String filterString = "(&(name=" + actionId + ")(type=Action))";
        try {
            logger.debug("filter: " + filterString);
            ServiceReference[] sRef = bundleContext.getAllServiceReferences(null, filterString);
            if(sRef != null && sRef.length > 0){
                action = (Action) bundleContext.getService(sRef[0]);
            }

        } catch (InvalidSyntaxException e) {
            String msg = "cannot create service filter: " + filterString;
            logger.error(msg , e);
            throw new IllegalStateException(msg);
        }

        return action;
    }
}
