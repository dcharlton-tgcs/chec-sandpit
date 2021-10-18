package com.tgcs.tgcp.bridge.common;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class XmlDocument {

    Document doc = null;

    /**
     * Load an xml string
     *
     * @param msg Message to parse
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void loadXml(String msg) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(msg.trim()));
        doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();

    }

    public Node selectSingleNode(String xPath) throws XPathExpressionException {
        if (doc != null && xPath != null && xPath.trim().length() > 0) {
            XPath xmlPath = XPathFactory.newInstance().newXPath();
            return (Node) xmlPath.evaluate(xPath, doc, XPathConstants.NODE);
        }
        return null;
    }

    /**
     * Retrieves a {@code NodeList} from an XML structure using XPath.
     *
     * @param xPath to evaluate
     * @return {@code NodeList} found using the {@code xPath} argument or null if {@code xPath} is empty.
     * @throws XPathExpressionException
     */
    public NodeList getNodeList(String xPath) throws XPathExpressionException {
        if (doc != null && xPath != null && xPath.trim().length() > 0) {
            XPath xmlPath = XPathFactory.newInstance().newXPath();
            return (NodeList) xmlPath.evaluate(xPath, doc.getDocumentElement(), XPathConstants.NODESET);
        }
        return null;
    }

    public String getNodeTextContent(String xPath) throws Exception {
        Node node = selectSingleNode(xPath);
        if (node != null) {
            return node.getTextContent();
        } else {
            throw new Exception("Missing node text content");
        }
    }

    public String getOrDefaultNodeTextContent(String xPath, String defaultValue) {
        try {
            return getNodeTextContent(xPath);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String getSilentNodeTextContent(String xPath) {
        try {
            return getNodeTextContent(xPath);
        } catch (Exception e) {
            return "";
        }
    }

    public String getOuterXml() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult strResult = new StreamResult(writer);
        transformer.transform(source, strResult);
        return writer.getBuffer().toString();
    }

    public Node getRoot() {
        return doc.getDocumentElement();
    }

    public String getRootNodeName() {
        String name = doc.getDocumentElement().getNodeName();
        int index = name.indexOf(":");
        if (index > -1) {
            name = name.substring(index + 1);
        }

        return name;
    }

    public boolean checkPathExists(String xPath) {
        XPath path = XPathFactory.newInstance().newXPath();
        try {
            return !path.evaluate(xPath, doc).isEmpty();
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    /**
     * Method that returns a map containing key/value pairs from XML structures, like the following:
     * <pre>
     * {@code
     * <AddTenderRequest>
     *   <ParameterExtension>
     *     <KeyValuePair>
     *       <Key>RVMBarcode</Key>
     *       <Value>123456789</Value>
     *     </KeyValuePair>
     *   </ParameterExtension>
     * </AddTenderRequest> }
     * </pre>
     * <p>
     * To get a map containing the data from the {@code /AddTenderRequest/ParameterExtension/KeyValuePair}, having the value from {@code
     * Key} as map key and {@code Value} as map value, the method needs to be called with the following parameters: {@code
     * getKeyValuePairMap("/AddTender/AddTenderRequest/ParameterExtension", "Key", "Value")}. The method will evaluate the XPath using the
     * {@code xPath} argument, obtaining a {@code NodeList} containing all the key/value pairs. It will then use the {@code keyName},
     * {@code valueName} in each found node to extract the map keys and values.
     *
     * @param xPath     to look for in the XML
     * @param keyName   of the XML tag representing the key data
     * @param valueName of the XML tag representing the value data
     * @return map containing key/value pair data or empty map if no nodes were found by {@code xPath} or XPath evaluation failed
     */
    public Map<String, String> getKeyValuePairMap(String xPath, String keyName, String valueName) {
        try {
            NodeList elementList = getNodeList(xPath);

            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < elementList.getLength(); i++) {
                Element item = (Element) elementList.item(i);
                String key = item.getElementsByTagName(keyName).item(0).getTextContent();
                String value = item.getElementsByTagName(valueName).item(0).getTextContent();
                map.put(key, value);
            }

            return map;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
