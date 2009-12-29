package nugoh.core;

import nugoh.sdk.ActionFactory;
import nugoh.sdk.ServiceInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 14, 2009
 * Time: 1:57:57 PM
 */
public class CoreActivator implements BundleActivator {
    private final Logger logger = LoggerFactory.getLogger(CoreActivator.class);

    private ServiceRegistration sRegFactory;
    private ServiceRegistration sRegServiceInfo;

    private ServiceRegistration registerFactory(BundleContext bundleContext, ActionFactory actionFactory) {
        Properties props = new Properties();
        props.put("type", "ActionFactory");
        props.put("name", "ActionFactory");
        props.put("MainApplication", "nugoh");
        logger.debug("registering: ActionFactory");
        return bundleContext.registerService(ActionFactory.class.getName(), actionFactory, props);
    }

    private ServiceRegistration registerServiceInfo(BundleContext bundleContext, ServiceInfo serviceInfo) {
        Properties props = new Properties();
        props.put("type", "ServiceInfo");
        props.put("name", "ServiceInfo");
        props.put("MainApplication", "nugoh");
        logger.debug("registering: ServiceInfo");
        return bundleContext.registerService(ServiceInfo.class.getName(), serviceInfo, props);
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        logger.info("STARTING FACTORY SERVICE");
        ActionFactory actionFactory = new ActionFactoryOSGIImpl(bundleContext);
        sRegFactory = registerFactory(bundleContext, actionFactory);
        if(sRegFactory == null){
            throw new IllegalStateException("Cannot resgister factory Service");
        }

        ServiceInfo serviceInfo = new ServiceInfoOSGIImpl(bundleContext);
        sRegServiceInfo = registerServiceInfo(bundleContext, serviceInfo);
        if(sRegServiceInfo == null){
            throw new IllegalStateException("Cannot resgister serviceInfo Service");
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        sRegFactory.unregister();
    }
}
