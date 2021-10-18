package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation.UnsupportedOperationResponse;
import com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation.UnsupportedOperationResult;
import com.tgcs.tgcp.bridge.common.ObjectToXmlConverter;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.util.TestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UnsupportedOperationHandler.class, XmlDocument.class})
public class UnsupportedOperationHandlerTest {

    @Autowired
    private UnsupportedOperationHandler unsupportedOperationHandler;

    @MockBean
    private TcpClient tcpClient;

    @Autowired
    private XmlDocument xmlMessage;

    @Captor
    ArgumentCaptor argumentCaptor;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        xmlMessage.loadXml(TestSupport.getString("unsupportedOperation/unsupportedOperationRequest.xml"));
    }

    @Test
    public void testProcessChecUnsupportedOperation() {
        unsupportedOperationHandler.processChecMessage(tcpClient, xmlMessage);
        verify(tcpClient).sendResponseToChecApp(any(JAXBElement.class));
    }

    @Test
    public void testProcessChecUnsupportedOperationDynamicNodes() throws ParserConfigurationException, SAXException, IOException {
        String rootNode = xmlMessage.getRootNodeName();
        String responseNode = rootNode + "Response";
        String resultNode = rootNode + "Result";

        unsupportedOperationHandler.processChecMessage(tcpClient, xmlMessage);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());
        JAXBElement notSupportedOperationResponse = (JAXBElement) argumentCaptor.getAllValues().get(0);

        String responseObject = ObjectToXmlConverter.jaxbObjectToXML(notSupportedOperationResponse, false);
        XmlDocument responseXml = new XmlDocument();
        responseXml.loadXml(responseObject);

        assertEquals(responseNode, responseXml.getRootNodeName());
        assertEquals(resultNode, responseXml.getRoot().getFirstChild().getNextSibling().getNodeName());
        assertEquals("133", responseXml.getSilentNodeTextContent(responseNode + "/" + resultNode + "/" + "RequestID"));
        assertEquals(rootNode + " not implemented", responseXml.getSilentNodeTextContent(responseNode + "/" + resultNode + "/" + "ExceptionResult/Message"));
        assertEquals(ErrorCode.UNSUPPORTED_OPERATION.getValue(), responseXml.getSilentNodeTextContent(responseNode + "/" + resultNode + "/" + "ExceptionResult/ErrorCode"));
    }

    @Test
    public void testCreateUnsupportedOperationResponse() {
        UnsupportedOperationResponse unsupportedOperationResponse = unsupportedOperationHandler.
                createExceptionUnsupportedOperationResponse("1", "UnsupportedOperation");

        assertNotNull(unsupportedOperationResponse);

        assertNotNull(unsupportedOperationResponse.getUnsupportedOperationResult());
        assertTrue(unsupportedOperationResponse.getUnsupportedOperationResult() instanceof JAXBElement);

        UnsupportedOperationResult unsupportedOperationResult =
                (UnsupportedOperationResult) ((JAXBElement<?>) unsupportedOperationResponse.getUnsupportedOperationResult()).getValue();

        assertEquals("1", unsupportedOperationResult.getRequestId());
        assertNotNull(unsupportedOperationResult.getExceptionResult());

        assertEquals("UnsupportedOperation not implemented",
                unsupportedOperationResult.getExceptionResult().getMessage());
        assertEquals(ErrorCode.UNSUPPORTED_OPERATION.getValue(),
                unsupportedOperationResult.getExceptionResult().getErrorCode());
    }

}