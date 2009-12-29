package nugoh.webgui.client.event;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import nugoh.webgui.client.bean.ServiceDescription;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 22, 2009
 * Time: 1:30:36 AM
 */
public class ServiceChangeEvent extends GwtEvent<ServiceChangeEvent.IServiceChangeHandler>{
    private JsArray<ServiceDescription> serviceDescriptionList;

    public interface IServiceChangeHandler extends EventHandler{
       void onServiceChange(ServiceChangeEvent event);
    }

    public static final GwtEvent.Type<ServiceChangeEvent.IServiceChangeHandler> TYPE = new GwtEvent.Type<IServiceChangeHandler>();

    public ServiceChangeEvent(JsArray<ServiceDescription> serviceDescriptionList) {
        this.serviceDescriptionList = serviceDescriptionList;
    }

    @Override
    public Type getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(IServiceChangeHandler eventHandler) {
        eventHandler.onServiceChange(this);
    }

    public JsArray<ServiceDescription> getServiceDescriptionList() {
        return serviceDescriptionList;
    }
}
