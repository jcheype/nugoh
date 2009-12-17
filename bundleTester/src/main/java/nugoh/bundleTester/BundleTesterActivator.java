package nugoh.bundleTester;

import nugoh.sdk.ActionFactory;
import nugoh.sdk.ActionNode;
import nugoh.sdk.XmlParser;
import nugoh.util.watcher.FileWatcher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 14, 2009
 * Time: 5:37:01 PM
 */
public class BundleTesterActivator implements BundleActivator {
    private final Logger logger = LoggerFactory.getLogger(BundleTesterActivator.class);
    private Thread fileWatcherThread;
    private ActionFactory factory;
    private XmlParser xmlParser = new XmlParser();

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        ServiceReference sRef = bundleContext.getServiceReference(ActionFactory.class.getName());
        factory = (ActionFactory) bundleContext.getService(sRef);

        File file = new File(System.getProperty("nugoh.config", "config.xml"));

        FileWatcher fileWatcher = new FileWatcher(file) {

            @Override
            protected void onChange(File file) {
                try {
                    List<ActionNode> actionNodeList = xmlParser.parse(new FileInputStream(file));
                    for (ActionNode an : actionNodeList) {
                        try {
                            factory.createAction(an, 5000, true);
                        } catch (Exception e) {
                            logger.error("cannot create action: ", an);
                        }
                    }
                } catch (Exception e) {
                    logger.error("cannot parse config file: ", e);
                }
            }
        };

        fileWatcherThread = new Thread(fileWatcher);
        fileWatcherThread.start();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        logger.info("Stop file watcherThread...");
        fileWatcherThread.interrupt();
        fileWatcherThread.join();
        logger.info("Stopped file watcherThread");
    }
}
