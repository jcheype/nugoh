package nugoh.webgui.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import nugoh.webgui.client.event.ServiceChangeEvent;
import nugoh.webgui.client.injector.GlobalHandlerManager;
import nugoh.webgui.client.bean.ServiceDescription;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 22, 2009
 * Time: 1:24:32 AM
 */
public class ServiceWatcher {
    private static final String SERVICEINFO_URL = "/serviceinfo";

    private final static String SIMULATION_JSON = "[{\"parameters\":[],\"defaultValueMap\":{},\"serviceName\":\"workflow\",\"subnodes\":true,\"properties\":false},{\"parameters\":[\"filter\",\"folder\",\"interval\",\"recursive\"],\"defaultValueMap\":{\"interval\":\"5000\",\"recursive\":\"false\",\"filter\":\"[^\\.].*\"},\"serviceName\":\"folderwatcher\",\"subnodes\":false,\"properties\":false},{\"parameters\":[\"name\"],\"defaultValueMap\":{},\"serviceName\":\"sample\",\"subnodes\":false,\"properties\":false},{\"parameters\":[\"rate\"],\"defaultValueMap\":{\"metaClass\":\"groovy.lang.MetaClassImpl@6e8af0b0[class Change]\"},\"serviceName\":\"change.groovy\",\"subnodes\":false,\"properties\":false},{\"parameters\":[\"cool\",\"hello\"],\"defaultValueMap\":{\"metaClass\":\"groovy.lang.MetaClassImpl@29d6399b[class Test]\"},\"serviceName\":\"test.groovy\",\"subnodes\":false,\"properties\":false},{\"parameters\":[],\"defaultValueMap\":{},\"serviceName\":\"test.js\",\"subnodes\":false,\"properties\":false}]";

    private String lastResponse = null;

    private final GlobalHandlerManager globalHandler;

    @Inject    
    public ServiceWatcher(GlobalHandlerManager globalHandler) {
        this.globalHandler = globalHandler;
        Timer timer = new Timer() {
            public void run() {
                updateRequest();
            }
        };

        timer.scheduleRepeating(1000);
    }

    public static native JsArray<ServiceDescription> parseJson(String jsonStr) /*-{
        return eval(jsonStr);
    }-*/;

    private void updateRequest() {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(SERVICEINFO_URL));

        try {
            Request request = builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                    Window.alert("Error");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        if (response.getText().equals(lastResponse)) {
                            return;
                        }
                        JsArray<ServiceDescription> serviceDescriptionList = parseJson(response.getText());
                        lastResponse = response.getText();
                        globalHandler.fireEvent(new ServiceChangeEvent(serviceDescriptionList));                        
                    } else {
                        if (response.getStatusCode() == 404) {
                            if (SIMULATION_JSON.equals(lastResponse)) {
                                return;
                            }
                            JsArray<ServiceDescription> serviceDescriptionList = parseJson(SIMULATION_JSON);
                            lastResponse = SIMULATION_JSON;
                            globalHandler.fireEvent(new ServiceChangeEvent(serviceDescriptionList));
                        } else {
                            Window.alert("Error: " + response.getStatusCode());
                        }
                    }
                }
            });
        } catch (RequestException ex) {
            Window.alert("Error");            
        }
    }
}