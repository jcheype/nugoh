package nugoh.scripting;

import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import nugoh.sdk.ActionInit;
import nugoh.sdk.PojoFactory;
import nugoh.util.watcher.FolderWatcher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 10:31:21 PM
 */
public class ScriptingActivator implements BundleActivator {
    private final Logger logger = LoggerFactory.getLogger(ScriptingActivator.class);
    private final Map<String, ServiceRegistration> serviceRegistrationMap = new HashMap();
    private Thread folderWatcherThread;
    private String scriptFolderPath = System.getProperty("nugoh.script", "scripts");

    private BundleContext bundleContext;
    private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    private PojoFactory getGroovyPojoFactory(String filename) throws groovy.util.ScriptException, ResourceException, IOException {
        GroovyScriptEngine gse = new GroovyScriptEngine(scriptFolderPath,
                this.getClass().getClassLoader());
        final Class groovyClass = gse.loadScriptByName(filename);
        return new PojoFactory() {
            @Override
            public Object newInstance() throws Exception {
                return groovyClass.newInstance();
            }
        };
    }

    private PojoFactory pojoFromScript(final File file) throws Exception {
        final String filename = file.getName();
        final String ext = filename.substring(filename.lastIndexOf('.') + 1);
        
        PojoFactory pojoFactory;
        if("groovy".equals(ext)){
            pojoFactory = getGroovyPojoFactory(filename);
        }
        else{
            pojoFactory = new PojoFactory() {

            @Override
            public Object newInstance() throws Exception {
                logger.debug("searching engine for ext: " + ext);                
                final ScriptEngine engine = scriptEngineManager.getEngineByExtension(ext);
                if (engine == null) {
                    String msg = "cannot find engine for extension: " + ext;
                    logger.error(msg);
                    throw new IllegalStateException();
                }
                engine.eval(new FileReader(file));
                Invocable inv = (Invocable) engine;
                return inv.getInterface(ActionInit.class);
            }
            };
        }
        return pojoFactory;
    }

    private ServiceRegistration registerPojo(Object pojo, String serviceId) {
        Dictionary props = new Properties();
        props.put("service", serviceId);
        props.put("type", "factory");

        return bundleContext.registerService(pojo.getClass().getName(), pojo, props);
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        
        File scriptFolder = new File(scriptFolderPath);
        if (!scriptFolder.isDirectory()) {
            throw new IllegalStateException("parameter \"nugoh.script\" must point to a directory: " + scriptFolderPath);
        }

        Runnable folderWatcher = new FolderWatcher(scriptFolder) {

            @Override
            protected void onChange(File file) {
                try {
                    PojoFactory pojoFactory = pojoFromScript(file);
                    ServiceRegistration previous = serviceRegistrationMap.get(file.getName());
                    if (previous != null) {
                        previous.unregister();
                    }
                    ServiceRegistration sReg = registerPojo(pojoFactory, file.getName());
                    serviceRegistrationMap.put(file.getName(), sReg);
                } catch (Exception e) {
                    logger.error("cannot load file: " + file.getAbsolutePath(), e);
                }
            }
        };
        folderWatcherThread = new Thread(folderWatcher);
        folderWatcherThread.start();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        logger.info("Stop folder watcherThread...");
        folderWatcherThread.interrupt();
        folderWatcherThread.join();
        logger.info("Stopped folder watcherThread");
    }
}
