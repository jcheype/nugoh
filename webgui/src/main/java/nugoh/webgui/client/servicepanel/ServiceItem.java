package nugoh.webgui.client.servicepanel;

import com.allen_sauer.gwt.dnd.client.*;
import com.google.gwt.user.client.ui.*;
import nugoh.webgui.client.bean.ServiceDescription;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 21, 2009
 * Time: 9:12:13 PM
 */
public class ServiceItem extends Composite implements HasDragHandle {
    private final ServiceDescription serviceDescription;
    private Label label;


    public ServiceItem(ServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
        label = new Label(serviceDescription.getName());

        label.setStyleName("serviceItem");
        label.addStyleName(serviceDescription.getColor());

        initWidget(label);
    }

    public ServiceDescription getServiceDescription() {
        return serviceDescription;
    }

    public Widget getDragHandle() {
        return label;
    }
}
