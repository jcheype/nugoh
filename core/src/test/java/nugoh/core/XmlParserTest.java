package nugoh.core;

import nugoh.sdk.ActionNode;
import nugoh.sdk.XmlParser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 10:52:34 AM
 */
public class XmlParserTest {
    private static final String RESOURCE_PATH = "/nugoh/core/test.xml";
    private List<Element> elements;
    private XmlParser xmlParser;

    @Before
    public void setup() throws Exception {
        elements = Util.getElements(RESOURCE_PATH);
        xmlParser = new XmlParser();
    }

    @Test
    public void parseElementTest() {
        ActionNode an = xmlParser.parse(elements.get(0));
        assertEquals(an.getAttributes().size(), 2);
        assertTrue("flow1".equals(an.getAttributes().get("id")));

        assertEquals(an.getActionNodes().size(), 3);

    }

        @Test
    public void parseInputStreamTest() throws Exception {
        List<ActionNode> anList = xmlParser.parse(getClass().getResourceAsStream(RESOURCE_PATH));
        ActionNode an = anList.get(0);

            assertEquals(an.getAttributes().size(), 2);
        assertTrue("flow1".equals(an.getAttributes().get("id")));

        assertEquals(an.getActionNodes().size(), 3);

    }

}
