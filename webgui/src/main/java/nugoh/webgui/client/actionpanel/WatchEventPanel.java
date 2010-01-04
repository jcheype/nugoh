package nugoh.webgui.client.actionpanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 30, 2009
 * Time: 2:55:46 AM
 */

public class WatchEventPanel extends VerticalPanel implements SourcesMouseEvents {

        private MouseListenerCollection mouseListeners;

        /**
         * constructor - extending VerticalPanel with event watching
         */
        public WatchEventPanel() {

                //watch this widget's mouse events
                sinkEvents(Event.MOUSEEVENTS);
        }

        /**
         * watch for browser events - from sinkEvents([MouseEvents])
         */
    public void onBrowserEvent(Event event) {

        switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEDOWN:
                        if (mouseListeners != null) {
                                        mouseListeners.fireMouseEvent(this, event);
                                }
                        break;
                case Event.ONMOUSEMOVE:
                        if (mouseListeners != null) {
                                        mouseListeners.fireMouseEvent(this, event);
                                }
                        break;
                case Event.ONMOUSEOUT:
                        break;
                case Event.ONMOUSEOVER:
                        break;
                case Event.ONMOUSEUP:
                        if (mouseListeners != null) {
                                        mouseListeners.fireMouseEvent(this, event);
                                }
                        break;
                case Event.ONMOUSEWHEEL:
                        break;
        }
    }

    /**
     * observer stuff
     */
        public void addMouseListener(MouseListener listener) {
                if (mouseListeners == null)
                        mouseListeners = new MouseListenerCollection();
                mouseListeners.add(listener);
        }

        public void removeMouseListener(MouseListener listener) {
                if (mouseListeners != null)
                        mouseListeners.remove(listener);
        }

}