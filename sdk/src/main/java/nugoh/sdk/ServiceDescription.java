package nugoh.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, String> defaultValueMap = new HashMap();

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

    public Map<String, String> getDefaultValueMap() {
        return defaultValueMap;
    }
}
