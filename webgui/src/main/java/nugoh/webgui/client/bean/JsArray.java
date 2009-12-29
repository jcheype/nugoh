package nugoh.webgui.client.bean;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 20, 2009
 * Time: 3:29:03 AM
 */
public class JsArray<E> extends JavaScriptObject {
  protected JsArray() { }

  public final native int length() /*-{ return this.length; }-*/;
  public final native E get(int i) /*-{ return this[i];     }-*/;
}