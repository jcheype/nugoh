package nugoh.webgui.client.injector;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import nugoh.webgui.client.MainPanel;
import nugoh.webgui.client.ServiceWatcher;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 21, 2009
 * Time: 11:12:04 PM
 */

@GinModules(MyClientModule.class)
public interface MyInjector extends Ginjector {
    MainPanel getMainPanel();
    ServiceWatcher getServiceWatcher();
}
