package nugoh.sdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 18, 2009
 * Time: 5:31:34 PM
 */
public class ServiceDescription {
    private String serviceName;
    private List<String> parameters = new ArrayList();
    private boolean subnodes = false;
    private boolean properties = false;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public boolean isSubnodes() {
        return subnodes;
    }

    public void setSubnodes(boolean subnodes) {
        this.subnodes = subnodes;
    }

    public boolean isProperties() {
        return properties;
    }

    public void setProperties(boolean properties) {
        this.properties = properties;
    }
}
