package nugoh.webgui.client.bean;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 20, 2009
 * Time: 3:25:11 AM
 */
public class ServiceDescription extends JavaScriptObject {
    // An overlay type

    // Overlay types always have protected, zero-arg ctors
    protected ServiceDescription() {
    }

    public final native String getName() /*-{ return this.serviceName; }-*/;

    public final String getColor(){
        return "color" + (Math.abs(getName().hashCode()) % 8 + 1);
    }

    public final native boolean isSubnodes() /*-{ return this.subnodes; }-*/;

    public final native JsArray<String> getAttributes() /*-{ return this.parameters;  }-*/;

    public final native String getDefaultValue(String attributeName) /*-{
        return this.defaultValueMap[attributeName];
    }-*/;
}
