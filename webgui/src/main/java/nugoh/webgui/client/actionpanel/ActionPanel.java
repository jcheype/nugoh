package nugoh.webgui.client.actionpanel;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.*;
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
    private final VerticalPanel panel = new VerticalPanel(){
        @Override
        public boolean remove(Widget w) {
            boolean result = super.remove(w);
            if(this.getWidgetCount() == 0 && result){
                addStyleName("empty");
            }
            return result;
        }
    };

    private final PickupDragController pickupDragController;
    private final ReorderDropController reorderDropController;

    private final ServicePanel servicePanel;
    private final DropController drop;

    public ActionPanel(final ServicePanel servicePanel) {
        pickupDragController = new PickupDragController(RootPanel.get(), false);
        reorderDropController = new ReorderDropController(panel);
        pickupDragController.registerDropController(reorderDropController);
        this.servicePanel = servicePanel;
        panel.addStyleName("actionPanel");

        drop = new SimpleDropController(panel) {
            @Override
            public void onDrop(DragContext context) {
                ActionItem actionItem = new ActionItem(((ServiceItem) context.selectedWidgets.get(0)).getServiceDescription(), servicePanel);
                pickupDragController.makeDraggable(actionItem);
                ActionPanel.this.add(actionItem);
            }
        };

        panel.addStyleName("empty");        
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
        panel.removeStyleName("empty");
        panel.add(actionItem);
    }

    public List<ActionItem> getActionItems() {
        List<ActionItem> result = new ArrayList();
        for (int i = 0; i < panel.getWidgetCount(); i++) {
            result.add((ActionItem) panel.getWidget(i));
        }

        return result;
    }

    public JSONArray toJSON(){
        JSONArray json = new JSONArray();
        for(ActionItem actionItem : getActionItems()){
            json.set(json.size(), actionItem.toJSON());
        }
        return json;
    }
}
