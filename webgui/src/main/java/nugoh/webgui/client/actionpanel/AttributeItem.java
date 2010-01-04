package nugoh.webgui.client.actionpanel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 22, 2009
 * Time: 8:33:36 AM
 */
public class AttributeItem extends Composite {
    private final String name;
    private final Label label;
    private final TextBox textBox = new TextBox();
    public AttributeItem(String labelText) {
        name = labelText;
        label = new Label(labelText + " : ");
        label.addStyleName("attributeLabel");
        HorizontalPanel hpanel = new HorizontalPanel();
        hpanel.add(label);
        hpanel.add(textBox);
        initWidget(hpanel);
    }

    public String getValue(){
        return textBox.getValue();
    }

    public String getName() {
        return name;
    }
}
