package nugoh.webgui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import nugoh.webgui.client.MainPanel;
import nugoh.webgui.client.injector.MyInjector;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application
        implements EntryPoint {

    private final MyInjector injector = GWT.create(MyInjector.class);

    private ServiceWatcher serviceWatcher;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        MainPanel mainPanel = injector.getMainPanel();
        serviceWatcher = injector.getServiceWatcher();
        
        RootPanel.get().add(mainPanel);
    }
}
