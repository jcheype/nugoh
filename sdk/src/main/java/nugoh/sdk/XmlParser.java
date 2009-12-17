package nugoh.sdk;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 10:09:54 AM
 */
public class XmlParser {

    private void parseAttributes(ActionNode an, Element e){
        NamedNodeMap attributes = e.getAttributes();
        for(int i=0 ; i < attributes.getLength() ; i++){
            Node node = attributes.item(i);
            an.addAttribute(node.getNodeName(), node.getNodeValue());
        }
    }

    private void parseSubNodes(ActionNode an, Element e){
        NodeList nodeList = e.getChildNodes();
        for(int i=0 ; i < nodeList.getLength() ; i++){
            Node node = nodeList.item(i);
            if("action".equals(node.getNodeName())){
                an.addAction(parse((Element)node));
            }
        }
    }

    public ActionNode parse(Element e){
        if(e == null){
            throw new IllegalStateException("cannot Parse null element");
        }
        if(!e.getNodeName().equals("action")){
            throw new IllegalStateException("cannot Parse non action element: " + e.getNodeName());
        }

        ActionNode result = new ActionNode();

        parseAttributes(result,e);
        parseSubNodes(result, e);

        return result;
    }

    public List<ActionNode> parse(InputStream is) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        List<ActionNode> actionNodes = new ArrayList<ActionNode>();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                if("action".equals(e.getNodeName())){
                    actionNodes.add(parse(e));
                }
            }
        }
        return actionNodes;
    }
}
