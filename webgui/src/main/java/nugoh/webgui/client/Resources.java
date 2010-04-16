package nugoh.webgui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
    public static final Resources INSTANCE =  GWT.create(Resources.class);

    @ClientBundle.Source("icons/control.png")
    ImageResource header();

    @ClientBundle.Source("icons/control-270.png")
    ImageResource headerDown();

    @ClientBundle.Source("icons/cross.png")
    ImageResource delete();

    @ClientBundle.Source("icons/wrench.png")
    ImageResource details();
}