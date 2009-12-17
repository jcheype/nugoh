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
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 5:53:33 PM
 */
public class RestfulServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(RestfulServlet.class);
    private NamedPattern namedPattern;
    private String serviceId;
    private ActionFactory factory;

    public void setFactory(ActionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String uriTemplate = config.getInitParameter("uri-template");
        logger.debug("URI Template: " + uriTemplate);
        namedPattern = new NamedPattern(uriTemplate);

        serviceId = config.getInitParameter("service-id");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = factory.retrieveAction(serviceId);
        logger.debug(req.getQueryString());
        try {
            action.run(namedPattern.getMap(req.getRequestURI()));
        } catch (Exception e) {
            String msg = "Error while trying run action";
            logger.error(msg, e);
            throw new ServletException(msg);
        }
        resp.getOutputStream().print(namedPattern.toString());
    }
}
