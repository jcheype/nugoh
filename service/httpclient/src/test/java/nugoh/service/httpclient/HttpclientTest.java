package nugoh.service.httpclient;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Apr 15, 2010
 * Time: 5:49:04 PM
 */
public class HttpclientTest {
    private final Logger logger = LoggerFactory.getLogger(HttpclientTest.class);
    
    HttpClientComponent httpClientComponent;

    @Before
    public void setup(){
        httpClientComponent = new HttpClientComponent();
    }

    @Test
    public void getMethod() throws IOException {
        // GET should be set by default
        // httpClientComponent.setMethod("GET");

        httpClientComponent.setUrl("http://google.fr");
        //httpClientComponent.setParamKey("params");


        httpClientComponent.init();
        
        Map<String, Object> context = new HashMap();
        //Map<String, Object> params = new HashMap();

        httpClientComponent.run(context);

        logger.debug("result: " + context.get("data"));
    }
}
