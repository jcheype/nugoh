package nugoh.webgui.client.servicepanel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import nugoh.webgui.client.bean.ServiceDescription;
import nugoh.webgui.client.event.ServiceChangeEvent;
import nugoh.webgui.client.injector.GlobalHandlerManager;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 18, 2009
 * Time: 3:46:55 PM
 */
public class ServicePanel extends Composite { // Could extend Widget instead

 


    final Panel listItem = new VerticalPanel();

    PickupDragController dragController;

    private final GlobalHandlerManager globalHandler;

    @Inject
    public ServicePanel(GlobalHandlerManager globalHandler) {
        this.globalHandler = globalHandler;

        globalHandler.addHandler(ServiceChangeEvent.TYPE, new ServiceChangeEvent.IServiceChangeHandler(){
            public void onServiceChange(ServiceChangeEvent event) {
                updateServiceList(event.getServiceDescriptionList());
            }
        });

        listItem.setStyleName("listItem");
        initWidget(listItem);

        dragController = new PickupDragController(RootPanel.get(), false);
        dragController.setBehaviorDragProxy(true);

    }

    public PickupDragController getDragController() {
        return dragController;
    }

    private void updateServiceList(JsArray<ServiceDescription> serviceDescriptionList) {
        listItem.clear();
        for (int i = 0; i < serviceDescriptionList.length(); i++) {
            ServiceItem item = new ServiceItem(serviceDescriptionList.get(i));
            dragController.makeDraggable(item);
            listItem.add(item);
            GWT.log(serviceDescriptionList.get(i).getName(), null);
        }
    }
}
