package nugoh.webgui.client.actionpanel;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import nugoh.webgui.client.servicepanel.ServiceItem;
import nugoh.webgui.client.servicepanel.ServicePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 26, 2009
 * Time: 5:19:32 PM
 */
public class ActionPanel extends Composite{
    private final VerticalPanel panel = new VerticalPanel();

    private final ServicePanel servicePanel;
    private final DropController drop;

    public ActionPanel(final ServicePanel servicePanel) {
        this.servicePanel = servicePanel;
        panel.addStyleName("actionPanel");
        panel.setHeight("20px");

        drop = new SimpleDropController(panel) {
            @Override
            public void onDrop(DragContext context) {
                ActionPanel.this.add(new ActionItem(((ServiceItem) context.selectedWidgets.get(0)).getServiceDescription(), servicePanel));
            }
        };
        initWidget(panel);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        servicePanel.getDragController().registerDropController(drop);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        servicePanel.getDragController().unregisterDropController(drop);
    }

    public void add(ActionItem actionItem) {
        panel.add(actionItem);
    }

    public List<ActionItem> getActionItems() {
        List<ActionItem> result = new ArrayList();
        for (int i = 0; i < panel.getWidgetCount(); i++) {
            result.add((ActionItem) panel.getWidget(i));
        }

        return result;
    }
}
