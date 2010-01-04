package nugoh.restful;

import nugoh.sdk.ActionFactory;
import nugoh.sdk.ServiceDescription;
import nugoh.sdk.ServiceInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.framework.BundleContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 18, 2009
 * Time: 5:10:19 PM
 */
public class ListActionServlet extends HttpServlet{
    private ServiceInfo serviceInfo;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mapper = new ObjectMapper();
    }

    public void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ServiceDescription> serviceDescriptionList = serviceInfo.getAllServiceInfo();
        mapper.writeValue(resp.getWriter(), serviceDescriptionList);
    }
}
