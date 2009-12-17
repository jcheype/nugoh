package nugoh.restful;

import nugoh.sdk.ActionFactory;
import nugoh.util.watcher.FileWatcher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 15, 2009
 * Time: 4:55:32 PM
 */
public class RestfulActivator implements BundleActivator{
    private final Logger logger = LoggerFactory.getLogger(RestfulActivator.class);

    private BundleContext bundleContext;
    private List<ServiceRegistration> serviceRegistrationList = new ArrayList();

    private Thread fileWatcherThread;

    private String getAlias(String uri){
        String alias = uri.substring(0,uri.indexOf('{')-1);
        return alias;
    }

    private ActionFactory getActionFactory() {
        ServiceReference sRef = bundleContext.getServiceReference(ActionFactory.class.getName());
        ActionFactory factory = (ActionFactory) bundleContext.getService(sRef);
        if (factory == null) {
            throw new IllegalStateException("No factory found");
        }
        return factory;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;

        String configPath = System.getProperty("nugoh.restful", "restful.properties");
        File configFile = new File(configPath);
        logger.info("using config file: " + configFile.getAbsolutePath());
        Runnable fileWatcher = new FileWatcher(configFile) {
            @Override
            protected void onChange(File file) {
                try {
                    reload(file);
                } catch (IOException e) {
                    logger.error("Cannot reload config file: " + file.getPath(), e);
                }
            }
        };

        fileWatcherThread = new Thread(fileWatcher);
        fileWatcherThread.start();
    }

    private void unregisterRestfulServlets() {
        synchronized (serviceRegistrationList) {
            for (ServiceRegistration sReg : serviceRegistrationList) {
                sReg.unregister();
            }
            serviceRegistrationList.clear();
        }
    }

    private void reload(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        unregisterRestfulServlets();
        for (Object key : properties.keySet()) {
            String uri = (String) properties.get(key);
            String alias = (String) key;
            ServiceRegistration sReg = registerRestfulServlet(alias, uri);
            synchronized (serviceRegistrationList){
                serviceRegistrationList.add(sReg);
            }
        }

    }

    private ServiceRegistration registerRestfulServlet(String serviceId, String uri) {
        String alias = getAlias(uri);
        Dictionary props = new Hashtable();
        props.put("alias", alias);
        //props.put("servlet-name", "RestfulServlet" + alias);
        props.put("init.uri-template", uri);
        props.put("init.service-id", serviceId);
        //props.put("factory", getActionFactory());

        RestfulServlet restfulServlet = new RestfulServlet();
        restfulServlet.setFactory(getActionFactory());
        return bundleContext.registerService(Servlet.class.getName(), restfulServlet, props);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        logger.info("Stop file watcherThread...");
        fileWatcherThread.interrupt();
        fileWatcherThread.join();
        logger.info("Stopped file watcherThread");

        logger.info("Unregister Restful Servlets...");
        unregisterRestfulServlets();
        logger.info("Unregistered Restful Servlets");
    }
}
