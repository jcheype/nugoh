package nugoh.webgui.client.actionpanel;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import nugoh.webgui.client.Resources;
import nugoh.webgui.client.bean.ServiceDescription;
import nugoh.webgui.client.servicepanel.ServicePanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nugoh.webgui.client.editinplace.EditInPlace;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 21, 2009
 * Time: 9:18:46 PM
 */
public class ActionItem extends Composite implements HasDragHandle {

    interface ActionItemUiBinder extends UiBinder<Widget, ActionItem> {
    }
    private static ActionItemUiBinder uiBinder = GWT.create(ActionItemUiBinder.class);
    private final ServiceDescription serviceDescription;
    private final List<AttributeItem> attributes = new ArrayList();
    private final List<ActionItem> actionItems = new ArrayList();
    private boolean showDetails = false;
    @UiField
    Panel mainPanel;
    @UiField
    DisclosurePanel disclosurePanel;
    @UiField
    Label title;
    @UiField
    Image delete;
    @UiField
    Image details;
    @UiField
    Panel attributesPanel;
    @UiField
    Panel header;
    @UiField
    FlowPanel content;
    @UiField
    Panel subActionsPanel;
    @UiField
    Image headerIcon;
    @UiField
    EditInPlace actionId;


    private final ActionPanel actionPanel;


    @UiHandler("delete")
    void deleteClick(ClickEvent event) {
        ActionItem.this.removeFromParent();
    }

    @UiHandler("details")
    void detailsClick(ClickEvent event) {
        toggleDetails();
        if (disclosurePanel.isOpen()) {
            event.stopPropagation();
        }
    }

    public void toggleDetails() {
        if (showDetails) {
            hideDefaultAttributes();
        } else {
            showDefaultAttributes();
        }
        showDetails = !showDetails;
    }

    public void hideDefaultAttributes() {
        for (AttributeItem attributeItem : attributes) {
            if (attributeItem.isDefault()) {
                attributeItem.setVisible(false);
            }
        }
    }

    public void showDefaultAttributes() {
        for (AttributeItem attributeItem : attributes) {
            if (attributeItem.isDefault()) {
                attributeItem.setVisible(true);
            }
        }
    }

    public ActionItem(ServiceDescription serviceDescription, ServicePanel servicePanel) {
        initWidget(uiBinder.createAndBindUi(this));

        disclosurePanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {

            public void onClose(CloseEvent<DisclosurePanel> disclosurePanelCloseEvent) {
                headerIcon.setResource(Resources.INSTANCE.header());
            }
        });

        disclosurePanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {

            public void onOpen(OpenEvent<DisclosurePanel> disclosurePanelOpenEvent) {
                headerIcon.setResource(Resources.INSTANCE.headerDown());
            }
        });

        header.addStyleName(serviceDescription.getColor());
        title.setText(serviceDescription.getName());

        for (int i = 0; i < serviceDescription.getAttributes().length(); i++) {
            String attributeName = serviceDescription.getAttributes().get(i);
            String value = serviceDescription.getDefaultValue(attributeName);
            AttributeItem attributeItem = new AttributeItem(attributeName, value);
            attributesPanel.add(attributeItem);
            attributes.add(attributeItem);
        }

        if (serviceDescription.isSubnodes()) {
            actionPanel = new ActionPanel(servicePanel);
            subActionsPanel.add(actionPanel);
        } else {
            actionPanel = null;
        }
        this.serviceDescription = serviceDescription;

        hideDefaultAttributes();
    }

    public String getId(){
        return actionId.getValue();
    }

    public Map<String, String> getAttributes() {
        Map<String, String> result = new HashMap();
        for (AttributeItem attributeItem : attributes) {
            result.put(attributeItem.getName(), attributeItem.getValue());
        }
        result.put("id", getId());

        return result;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public Widget getDragHandle() {
        return title;
    }


    public JSONValue toJSON() {
        JSONObject json = new JSONObject();
        JSONObject attributesJSON = new JSONObject();
        for (AttributeItem attributeItem : attributes) {
            if (attributeItem.getValue() != null && attributeItem.getValue().trim().length() > 0) {
                attributesJSON.put(attributeItem.getName(), new JSONString(attributeItem.getValue()));
            }
        }
        if (getId().length() > 0) {
            attributesJSON.put("id", new JSONString(getId()));
        }

        json.put("attributes", attributesJSON);
        if (actionPanel != null) {
            json.put("subnodes", actionPanel.toJSON());
        }
        return json;
    }
}
