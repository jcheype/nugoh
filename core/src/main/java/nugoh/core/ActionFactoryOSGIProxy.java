package nugoh.core;

import nugoh.sdk.Action;
import nugoh.sdk.ActionFactory;
import nugoh.sdk.ActionNode;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 15, 2009
 * Time: 10:58:58 AM
 */
public class ActionFactoryOSGIProxy implements ActionFactory {
    private final BundleContext bundleContext;
    private final ServiceReference sRef;

    public ActionFactoryOSGIProxy(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        sRef = bundleContext.getServiceReference(ActionFactory.class.getName());
    }

    private ActionFactory getFactory(){
        ActionFactory result = (ActionFactory) bundleContext.getService(sRef);
        if(result == null){
            throw new IllegalStateException("no action factory available");
        }
        return result;
    }

    @Override
    public Action createAction(ActionNode actionNode, long timeout, boolean autoRegister) throws Exception {
        return getFactory().createAction(actionNode, timeout, autoRegister);
    }

    @Override
    public void registerAction(Action action, String id) throws Exception {
        getFactory().registerAction(action, id);
    }

    @Override
    public void unregisterAction(String id) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Action retrieveAction(String actionId) {
        return getFactory().retrieveAction(actionId);
    }
}
