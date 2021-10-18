package com.tgcs.tgcp.bridge.common;

import com.tgcs.tgcp.bridge.checoperations.model.receipt.AddReceiptLines;
import com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation.UnsupportedOperationResponse;
import com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend.VoidTransactionResponse;
import com.tgcs.tgcp.bridge.util.TestSupport;
import org.junit.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import static org.junit.Assert.*;

public class ObjectToXmlConverterTest {

    String soepHeader = "soeps~Session-Id=1|Message-Type=RESP|~";
    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<schema:VoidTransactionResponse xmlns:schema=\"http://bc.si.retail.ibm.com/POSBCSchema\"/>";

    @Test
    public void jaxbObjectToXMLTest() {
        String xml = soepHeader + testXml;
        assertEquals(xml, ObjectToXmlConverter.jaxbObjectToXML(new VoidTransactionResponse()));
    }

    @Test
    public void jaxbObjectToXMLWithoutSoepHeaderTest() {
        assertEquals(testXml, ObjectToXmlConverter.jaxbObjectToXML(new VoidTransactionResponse(), false));
    }

    @Test
    public void jaxbObjectToXMLReturnNullTest() {
        assertNull(ObjectToXmlConverter.jaxbObjectToXML(new Object(), false, false));
    }

    @Test
    public void jaxbObjectToXMLWithNullObjectTest() {
        assertNull(ObjectToXmlConverter.jaxbObjectToXML(null, false, false));
    }

    @Test
    public void jaxbObjectToXMLWithoutFormattedAndSoepTest() {
        assertEquals(testXml, ObjectToXmlConverter.jaxbObjectToXML(new VoidTransactionResponse(), false, false));
    }

    @Test
    public void jaxbObjectToXMLReturnWithSoepTest() {
        String xml = soepHeader + testXml;
        assertEquals(xml, ObjectToXmlConverter.jaxbObjectToXML(new VoidTransactionResponse(), false, true));
    }

    @Test
    public void jaxbJAXBElementToXMLTest() {
        String xml = soepHeader + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<test xmlns:ns2=\"http://bc.si.retail.ibm.com/POSBCSchema\"/>";
        assertEquals(xml, ObjectToXmlConverter.jaxbObjectToXML(new JAXBElement<>(
                new QName("test"), UnsupportedOperationResponse.class, new UnsupportedOperationResponse())));
    }

    @Test
    public void addSoepHeaderTest() {
        String addSoepHeaderTestMsg = soepHeader + testXml;
        assertTrue(addSoepHeaderTestMsg.equalsIgnoreCase(ObjectToXmlConverter.addSoepHeader(testXml, "RESP")));
    }

    @Test(expected = Exception.class)
    public void testJaxbXMLToObjectNullMessage() throws Exception {
        ObjectToXmlConverter.jaxbXMLToObject(null, AddReceiptLines.class);
    }

    @Test(expected = Exception.class)
    public void testJaxbXMLToObjectNullObject() throws Exception {
        XmlDocument xmlDocument = new XmlDocument();
        xmlDocument.loadXml(TestSupport.getString("receipt/addReceiptLinesRequest.xml"));
        ObjectToXmlConverter.jaxbXMLToObject(xmlDocument, null);
    }

    @Test
    public void testJaxbXMLToObject() throws Exception {
        XmlDocument xmlDocument = new XmlDocument();
        xmlDocument.loadXml(TestSupport.getString("receipt/addReceiptLinesRequest.xml"));
        Object object = ObjectToXmlConverter.jaxbXMLToObject(xmlDocument, AddReceiptLines.class);
        assertNotNull(object);
        assertTrue(object instanceof AddReceiptLines);
    }

    @Test
    public void testCreateJAXBElementResponse() {
        JAXBElement jaxbElement = ObjectToXmlConverter.createDynamicNodeName("test", new UnsupportedOperationResponse());

        assertNotNull(jaxbElement);
        assertEquals("testResponse", jaxbElement.getName().toString());
        assertEquals(UnsupportedOperationResponse.class, jaxbElement.getDeclaredType());
    }


}