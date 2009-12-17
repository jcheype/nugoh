package nugoh.core;

import nugoh.sdk.ActionInit;
import nugoh.sdk.ActionNode;
import nugoh.sdk.XmlParser;
import nugoh.service.sample.SimplePojo;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 6:49:33 PM
 */
public class ActionProxyTest {
    private List<Element> elements;
    private XmlParser xmlParser;

    @Before
    public void setup() throws Exception{
        elements = Util.getElements("/nugoh/core/test.xml");
        xmlParser = new XmlParser();
    }

    @Test
    public void actionProxy() throws Exception{
        ActionNode an = xmlParser.parse(elements.get(1));
        ActionInit actionInit = new ActionProxy(new SimplePojo(), an);

        Map<String, Object> map = new HashMap();
        actionInit.init();
        actionInit.run( map );

        String text = (String) map.get("helloInit");
        assertEquals("hello foo", text);
    }

    @Test(expected = IllegalStateException.class)
    public void actionProxyNoInit() throws Exception{

        ActionNode an = xmlParser.parse(elements.get(1));
        ActionInit actionInit = new ActionProxy(new SimplePojo(), an);

        Map<String, Object> map = new HashMap();

        actionInit.run( map );
    }


    @Test
    public void actionProxyActionNodeInjection() throws Exception{
        ActionNode an = xmlParser.parse(elements.get(1));
        SimplePojoWithActionNode pojo = new SimplePojoWithActionNode();
        ActionInit actionInit = new ActionProxy(pojo, an);

        assertNotNull(pojo.getActionNode());

    }

    private class SimplePojoWithActionNode extends SimplePojo{
        private ActionNode actionNode;

        public ActionNode getActionNode() {
            return actionNode;
        }

        public void setActionNode(ActionNode actionNode) {
            this.actionNode = actionNode;
        }
    }
}
