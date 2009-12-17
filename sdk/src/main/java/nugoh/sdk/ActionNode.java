package nugoh.sdk;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 9:58:43 AM
 */
public class ActionNode {
    private final List<ActionNode> actionNodes = new ArrayList();
    private final Map<String,String> attributes = new HashMap();
    private Properties properties = null;

    public List<ActionNode> getActionNodes() {
        return actionNodes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void addAction( ActionNode an){
        actionNodes.add(an);
    }

    public void addAttribute(String name, String value){
        attributes.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionNode)) return false;

        ActionNode that = (ActionNode) o;

        if (!actionNodes.equals(that.actionNodes)) return false;
        if (!attributes.equals(that.attributes)) return false;
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = actionNodes.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
