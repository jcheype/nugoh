/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nugoh.webgui.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.inject.Inject;
import nugoh.webgui.client.actionpanel.ActionPanel;

/**
 *
 * @author mush
 */
public class MainMenuBar extends Composite {

    @Inject
    public MainMenuBar(final ActionPanel actionPanel) {
        MenuBar menu = new MenuBar();
        menu.setAutoOpen(true);
        menu.setWidth("99%");
        menu.setAnimationEnabled(true);


        MenuBar fileMenu = new MenuBar(true);
        fileMenu.setAnimationEnabled(true);
        menu.addItem(new MenuItem("File", fileMenu));

        fileMenu.addItem("new", new Command() {

            public void execute() {
                Window.alert("json: " + actionPanel.toJSON().toString());
            }
        });

        fileMenu.addSeparator();

        fileMenu.addItem("open", new Command() {

            public void execute() {
                Window.alert("json: " + actionPanel.toJSON().toString());
            }
        });

        fileMenu.addSeparator();

        fileMenu.addItem("save", new Command() {

            public void execute() {
                Window.alert("json: " + actionPanel.toJSON().toString());
            }
        });

        fileMenu.addItem("save as", new Command() {

            public void execute() {
                Window.alert("json: " + actionPanel.toJSON().toString());
            }
        });



        initWidget(menu);
    }
}
