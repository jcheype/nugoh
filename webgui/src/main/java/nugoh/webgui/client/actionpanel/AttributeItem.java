package nugoh.webgui.client.actionpanel;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
    private final String defaultValue;
    private final HorizontalPanel hpanel = new HorizontalPanel();

    public boolean isDefault() {
        return (defaultValue != null) && defaultValue.equals(textBox.getValue());
    }

    public AttributeItem(String labelText, String value) {
        name = labelText;
        defaultValue = value;
        label = new Label(labelText + " : ");
        label.addStyleName("attributeLabel");
        hpanel.add(label);
        hpanel.add(textBox);

        if (value != null) {
            textBox.setValue(value);
            hpanel.addStyleName("defaultValue");
        }

        textBox.addKeyUpHandler(new KeyUpHandler() {

            public void onKeyUp(KeyUpEvent event) {
                if (defaultValue != null) {
                    if (defaultValue.equals(textBox.getValue())) {
                        hpanel.addStyleName("defaultValue");
                    } else {
                        hpanel.removeStyleName("defaultValue");
                    }
                }
            }
        });

        initWidget(hpanel);
    }

    public String getValue() {
        return textBox.getValue();
    }

    public String getName() {
        return name;
    }
}
