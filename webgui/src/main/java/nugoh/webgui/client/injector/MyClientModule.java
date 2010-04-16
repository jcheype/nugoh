package nugoh.webgui.client.injector;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import nugoh.webgui.client.ServiceWatcher;
import nugoh.webgui.client.actionpanel.ActionPanel;
import nugoh.webgui.client.servicepanel.ServicePanel;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 21, 2009
 * Time: 11:16:47 PM
 */
public class MyClientModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(ServicePanel.class).in(Singleton.class);
        bind(GlobalHandlerManager.class).in(Singleton.class);
        bind(ServiceWatcher.class).in(Singleton.class);
        bind(ActionPanel.class).in(Singleton.class);
    }
}
