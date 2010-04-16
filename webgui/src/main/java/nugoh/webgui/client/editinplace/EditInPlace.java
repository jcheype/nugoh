/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nugoh.webgui.client.editinplace;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 *
 * @author mush
 */
public class EditInPlace extends Composite {

    private static final String EMPTY_LABEL = "[ ]";
    private final TextBox textBox = new TextBox();
    private final Label label = new Label(EMPTY_LABEL);
    private final DeckPanel panel = new DeckPanel();

    public EditInPlace() {
        label.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                textBox.setText(getValue());
                panel.showWidget(0);
                textBox.setFocus(true);
                event.stopPropagation();
            }
        });

        textBox.addKeyDownHandler(new KeyDownHandler() {

            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    String value = textBox.getText().trim();
                    if ("".equals(value)) {
                        value = EMPTY_LABEL;
                    }
                    label.setText(value);
                    panel.showWidget(1);
                    event.stopPropagation();
                    event.preventDefault();
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                    panel.showWidget(1);
                    event.stopPropagation();
                }
            }
        });


        textBox.setVisible(
            false);

        panel.add(textBox);
        panel.add(label);
        panel.showWidget(
            1);
        initWidget(panel);
    }

    public String getValue() {
        String value = label.getText().trim();
        if (EMPTY_LABEL.equals(value)) {
            value = "";
        }
        return value;
    }

    public void setValue(String value) {
        textBox.setValue(value);
        label.setText(value);
    }
}
