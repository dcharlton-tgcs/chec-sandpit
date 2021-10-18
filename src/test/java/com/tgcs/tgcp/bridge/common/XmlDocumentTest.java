package com.tgcs.tgcp.bridge.common;

import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {XmlDocument.class})
public class XmlDocumentTest {

    @Autowired
    XmlDocument xmlDocument;

    String testXml = "<AddItem>" +
            "<AddItemRequest>" +
            "<RequestID>173</RequestID>" +
            "<ItemIdentifier>" +
            "<BarCode>" +
            "<ScanDataLabel>4311596474738</ScanDataLabel>" +
            "<ScanDataType>SCAN_SDT_UPCA</ScanDataType>" +
            "</BarCode>" +
            "</ItemIdentifier>" +
            "</AddItemRequest>" +
            "</AddItem>";

    @Before
    public void setUp() throws Exception {
        xmlDocument.loadXml(testXml);
    }

    @Test(expected = SAXParseException.class)
    public void loadXmlThrowsSaxExceptionOnInvalidXMLTest() throws Exception {
        new XmlDocument().loadXml("test");
    }

    @Test
    public void loadXmlValidXMLTest() throws Exception {
        xmlDocument.loadXml(testXml);
    }

    @Test
    public void getNodeTextContentTest() throws Exception {
        assertEquals("173", xmlDocument.getNodeTextContent("AddItem/AddItemRequest/RequestID"));
    }

    @Test(expected = Exception.class)
    public void getNodeTextContentThrowsExceptionIfWrongPathTest() throws Exception {
        xmlDocument.getNodeTextContent("AddItem/RequestID");
    }

    @Test
    public void getSilentNodeTextContentTest() {
        assertEquals("173", xmlDocument.getSilentNodeTextContent("AddItem/AddItemRequest/RequestID"));
    }

    @Test
    public void getSilentNodeTextContentReturnEmptyIfWrongPathTest() {
        assertEquals("", xmlDocument.getSilentNodeTextContent("AddItem/RequestID"));
    }

    @Test
    public void getOuterXmlTest() throws TransformerException {
        String outerXml = xmlDocument.getOuterXml().replaceAll("[\r\n ]", "");
        assertTrue(outerXml.trim().contains(testXml.trim()));
    }

    @Test
    public void getOuterXmlAddXmlHeaderTest() throws TransformerException {
        String outerXml = xmlDocument.getOuterXml().replaceAll("[\r\n ]", "");
        assertFalse(outerXml.trim().equalsIgnoreCase(testXml.trim()));
    }

    @Test
    public void getRootNameTest() {
        assertEquals("AddItem", xmlDocument.getRootNodeName());
    }

    @Test
    public void getInvalidRootNameTest() {
        assertNotEquals("AddItemRequest", xmlDocument.getRootNodeName());
    }

    @Test
    public void getRootNameWithoutSchemaTest() throws ParserConfigurationException, SAXException, IOException {
        xmlDocument.loadXml("<scsns:Initialize></scsns:Initialize>");
        assertEquals("Initialize", xmlDocument.getRootNodeName());
    }

    @Test
    public void checkGoodPathExistsTest() {
        assertTrue(xmlDocument.checkPathExists("AddItem/AddItemRequest/RequestID"));
    }

    @Test
    public void checkPathExistSReturnFalseTest() {
        assertFalse(xmlDocument.checkPathExists("AddItem/AddItemRequest/Request"));
    }

    @Test
    public void checkPathExistSXpathBadFormatReturnFalseTest() {
        assertFalse(xmlDocument.checkPathExists("AddItem/AddItemRequest///RequestID"));
    }

    @Test
    public void getRootTest() {
        assertNotNull(xmlDocument.getRoot());
        assertEquals("AddItem", xmlDocument.getRoot().getNodeName());
    }

    @Test
    public void getOrDefaultNodeTextContentTest() {
        assertEquals("173", xmlDocument.getOrDefaultNodeTextContent("AddItem/AddItemRequest/RequestID", "20"));
        assertEquals("20", xmlDocument.getOrDefaultNodeTextContent("AddItem/RequestID", "20"));
    }

    @Test
    public void selectNodeWhenNoXmlLoadedTest() throws XPathExpressionException {
        xmlDocument = new XmlDocument();
        assertNull(xmlDocument.selectSingleNode("AddItem/AddItemRequest/RequestID"));
    }

    @Test
    public void getKeyValuePairMapTest() throws ParserConfigurationException, SAXException, IOException {
        xmlDocument = new XmlDocument();
        xmlDocument.loadXml("<AddTender>\n"
                + "    <AddTenderRequest>\n"
                + "        <ParameterExtension>\n"
                + "          <KeyValuePair>\n"
                + "            <Key>RVMBarcode</Key>\n"
                + "            <Value>123456789</Value>\n"
                + "          </KeyValuePair>\n"
                + "        </ParameterExtension>\n"
                + "    </AddTenderRequest>\n"
                + "</AddTender>");
        Map<String, String> keyValuePairMap = xmlDocument.getKeyValuePairMap("/AddTender/AddTenderRequest/ParameterExtension/KeyValuePair",
                "Key",
                "Value");
        assertNotNull(keyValuePairMap);
        assertEquals("123456789", keyValuePairMap.get("RVMBarcode"));
        assertEquals("123456789", xmlDocument.getSilentNodeTextContent(ChecMessagePath.ADD_TENDER_REQUEST_PARAMETER_EXTENSION + "/" + ChecMessagePath.KEY_VALUE_PAIR_NAME + "/" + ChecMessagePath.VALUE_NAME));
    }

    @Test
    public void getKeyValuePairMapMalformedInputTest() throws ParserConfigurationException, SAXException, IOException {
        xmlDocument = new XmlDocument();
        xmlDocument.loadXml("<a><b></b></a>");
        Map<String, String> keyValuePairMap = xmlDocument.getKeyValuePairMap("/a/c", "non", "existent");
        assertNotNull(keyValuePairMap);
        assertEquals(0, keyValuePairMap.size());
    }

}
