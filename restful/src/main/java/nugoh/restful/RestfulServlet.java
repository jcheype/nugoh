package nugoh.restful;

import nugoh.sdk.Action;
import nugoh.sdk.ActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 5:53:33 PM
 */
public class RestfulServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(RestfulServlet.class);

    private ActionFactory actionFactory;
    private File configFile;
    private long lastModified = 0;

    private Map<String, NamedPattern> aliasMap = new HashMap();
    private Map<String, String> serviceIdMap = new HashMap();

    public void setActionFactory(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    private String getAliasFromRestfulUri(String uri) {
        String alias = uri.substring(0, uri.indexOf('{') - 1);
        return  alias;
    }

    synchronized private void reloadConfig() throws IOException {
        if (lastModified != configFile.lastModified()) {
            lastModified = configFile.lastModified();

            Properties properties = new Properties();
            properties.load(new FileInputStream(configFile));

            aliasMap.clear();
            serviceIdMap.clear();
            for (Object key : properties.keySet()) {
                String serviceId = (String) key;
                String restfulUri = properties.getProperty(serviceId);
                String alias = getAliasFromRestfulUri(restfulUri);
                logger.debug("adding alias: " + alias);
                aliasMap.put(alias, new NamedPattern(restfulUri));
                serviceIdMap.put(alias, serviceId);
            }
        }
    }

    private String getAliasFormQS(String queryString){
        logger.debug("searching alias: " + queryString);
        if(queryString == null || queryString.length() < 1){
            return null;
        }

        if(aliasMap.containsKey(queryString)){
            return queryString;
        }

        String newQueryString = queryString.substring(0,queryString.lastIndexOf('/'));
        return getAliasFormQS(newQueryString);
    }

    private void checkConfigFile() throws ServletException, IOException {
        if (!configFile.exists()) {
            logger.error("Configuration file does not exists: " + configFile.getAbsolutePath());
            throw new ServletException("Configuration file does not exists");
        }

        if (lastModified != configFile.lastModified()) {
            reloadConfig();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String configPath = System.getProperty("nugoh.restful", "restful.properties");
        configFile = new File(configPath);
        logger.info("using config file: " + configFile.getAbsolutePath());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkConfigFile();
        String requestURI = req.getRequestURI().substring(req.getServletPath().length());
        logger.debug(requestURI);
        String alias = getAliasFormQS(requestURI);
        if(alias == null){
            throw new ServletException("no alias for queryString: " + requestURI);
        }

        Action action = actionFactory.retrieveAction(serviceIdMap.get(alias));
        try {
            action.run(aliasMap.get(alias).getMap(requestURI));
        } catch (Exception e) {
            String msg = "Error while trying run action";
            logger.error(msg, e);
            throw new ServletException(msg);
        }
        resp.getOutputStream().print(aliasMap.get(alias).toString());
    }
}
