package nugoh.webgui.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import nugoh.webgui.client.actionpanel.ActionPanel;
import nugoh.webgui.client.servicepanel.ServicePanel;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 21, 2009
 * Time: 11:13:53 PM
 */
public class MainPanel extends Composite {

    private final ServicePanel servicePanel;
    private final ActionPanel actionPanel;

    @Inject
    public MainPanel(final ServicePanel servicePanel, final ActionPanel actionPanel, final MainMenuBar menu) {
        this.servicePanel = servicePanel;
        this.actionPanel = actionPanel;

        final SplitLayoutPanel dock = new SplitLayoutPanel();
        dock.addStyleName("mainPanel");

        dock.addNorth(menu, 25);

        final DecoratorPanel servicePanelDecorator = new DecoratorPanel();
        servicePanelDecorator.setWidget(this.servicePanel);
        servicePanelDecorator.addStyleName("sidePanelDecorator");

        dock.addWest(servicePanelDecorator, 120);


        final DecoratorPanel centerPanelDecorator = new DecoratorPanel();
        centerPanelDecorator.addStyleName("centerPanelDecorator");

        centerPanelDecorator.setWidget(actionPanel);
        dock.add(centerPanelDecorator);



        initWidget(dock);
    }
}
