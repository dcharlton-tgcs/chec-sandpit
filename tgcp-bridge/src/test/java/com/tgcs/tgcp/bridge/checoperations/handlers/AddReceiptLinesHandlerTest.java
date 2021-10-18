package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.AddReceiptLinesResponse;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.util.TestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AddReceiptLinesHandler.class, ExceptionHandler.class})
@SpringBootTest(classes = {ConfProperties.class,})
public class AddReceiptLinesHandlerTest {

    @Autowired
    private AddReceiptLinesHandler addReceiptLinesHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private XmlDocument xmlDocument;

    @MockBean
    private BridgeSession bridgeSession;


    @Captor
    ArgumentCaptor argumentCaptor;

    @Test
    public void testSendProcessChecMessage() throws TransformerException {
        when(xmlDocument.getOuterXml()).thenReturn(TestSupport.getString("receipt/addReceiptLinesRequest.xml"));
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_RECEIPT_LINES_REQUEST_ID, "")).thenReturn("11");
        addReceiptLinesHandler.setBridgeSession(bridgeSession);

        addReceiptLinesHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient, times(2)).sendResponseToChecApp(argumentCaptor.capture());

        POSReceiptEvent posReceiptEvent = (POSReceiptEvent) argumentCaptor.getAllValues().get(0);

        assertEquals("11", posReceiptEvent.getGroup());
        assertEquals(1, posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().size());
        assertEquals("LineItem", posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().get(0).getLineCategory());
        assertEquals("PersistentAdditionalLine", posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().get(0).getLineType());
        assertEquals("AGE RESTRICTED: 18", posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().get(0).getTextReceiptLine().getText());
        assertEquals(Boolean.TRUE, posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().get(0).getTextReceiptLine().getBold());

        assertEquals("11", ((AddReceiptLinesResponse) argumentCaptor.getValue()).getAddReceiptLinesResult().getRequestId());
    }

    @Test
    public void testSendProcessChecMessageError() {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_RECEIPT_LINES_REQUEST_ID, "")).thenReturn("11");
        addReceiptLinesHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertEquals("11", ((AddReceiptLinesResponse) argumentCaptor.getValue()).getAddReceiptLinesResult().getRequestId());

    }
}