package nugoh.webgui.client.actionpanel;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractInsertPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 30, 2009
 * Time: 2:28:08 AM
 */
public class ReorderDropController extends AbstractInsertPanelDropController {
    public ReorderDropController(InsertPanel dropTarget) {
        super(dropTarget);
    }

    @Override
    protected LocationWidgetComparator getLocationWidgetComparator() {
        return LocationWidgetComparator.BOTTOM_HALF_COMPARATOR;
    }

    @Override
    protected Widget newPositioner(DragContext dragContext) {
        HTML positioner = new HTML("<hr />");
        return positioner;
    }
}
