package nugoh.webgui.client.actionpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import nugoh.webgui.client.bean.ServiceDescription;
import nugoh.webgui.client.servicepanel.ServicePanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gwt.query.client.Effects.Effects;
import static com.google.gwt.query.client.Effects.Speed;
import static com.google.gwt.query.client.GQuery.$;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 21, 2009
 * Time: 9:18:46 PM
 */
public class ActionItem extends Composite {
    interface ActionItemUiBinder extends UiBinder<Widget, ActionItem> {
    }

    private static ActionItemUiBinder uiBinder = GWT.create(ActionItemUiBinder.class);


    private final ServiceDescription serviceDescription;

    private final List<AttributeItem> attributes = new ArrayList();
    private final List<ActionItem> actionItems = new ArrayList();

    @UiField
    Label title;

    @UiField
    Button delete;

    @UiField
    Panel attributesPanel;

    @UiField
    Panel header;

    @UiField
    Panel content;

    @UiField
    Panel subActionsPanel;


    @UiHandler("delete")
    void deleteClick(ClickEvent event) {
        $(ActionItem.this.getElement()).as(Effects).slideUp(Speed.FAST, new Function(){
            @Override
            public void f(Element e) {
                ActionItem.this.removeFromParent();
            }
        });
    }

    @UiHandler("title")
    void expandClick(ClickEvent event) {
        $(content.getElement()).as(Effects).slideToggle(Speed.FAST);
    }

    public ActionItem(ServiceDescription serviceDescription, ServicePanel servicePanel) {
        initWidget(uiBinder.createAndBindUi(this));
        header.addStyleName("color" + serviceDescription.getName().hashCode() % 8);
        title.setText(serviceDescription.getName());

        for (int i = 0; i < serviceDescription.getAttributes().length(); i++) {
            AttributeItem attributeItem = new AttributeItem(serviceDescription.getAttributes().get(i));
            attributesPanel.add(attributeItem);
            attributes.add(attributeItem);
        }

        if (serviceDescription.isSubnodes()) {
            subActionsPanel.add(new ActionPanel(servicePanel));
        }
        this.serviceDescription = serviceDescription;
    }

    public Map<String, String> getAttributes() {
        Map<String, String> result = new HashMap();
        for (AttributeItem attributeItem : attributes) {
            result.put(attributeItem.getName(), attributeItem.getValue());
        }
        return result;
    }

    public Panel getHeader() {
        return header;
    }

    public Panel getContent() {
        return content;
    }
}
