package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReprintReceiptsResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.util.TestSupport;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ReprintReceiptsHandler.class, XmlDocument.class})
public class ReprintReceiptsHandlerTest {

    @Autowired
    private ReprintReceiptsHandler reprintReceiptsHandler;

    @MockBean
    private TcpClient tcpClient;

    @Autowired
    private XmlDocument xmlMessage;

    @MockBean
    private IWebService webService;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        xmlMessage.loadXml(TestSupport.getString("receipt/reprintReceiptsRequest.xml"));
    }

    @Test
    public void testProcessChecMessageSendsReprintReceiptsResponse() {
        reprintReceiptsHandler.processChecMessage(tcpClient, xmlMessage);

        verify(tcpClient).sendResponseToChecApp(any(ReprintReceiptsResponse.class));
    }

    @Test
    public void testProcessChecMessageSendsReprintReceiptsResponseError() throws Exception {
        doThrow(new Exception("No transaction receipts were available.")).when(webService).printReceipt();

        reprintReceiptsHandler.processChecMessage(tcpClient, xmlMessage);

        verify(tcpClient).sendResponseToChecApp(any(ReprintReceiptsResponse.class));
    }

    @Test
    public void testThrowsForPrintReceipt() throws Exception {
        Exception exception = new Exception("No transaction receipts were available.");
        doThrow(exception).when(webService).printReceipt();

        assertThatExceptionOfType(ChecOperationException.class)
                .isThrownBy(() -> reprintReceiptsHandler.printReceipt())
                .matches(e -> e.getErrorCode().equals(ErrorCode.TRANSACTION_RECEIPTS_NOT_AVAILABLE.name()))
                .matches(e -> e.getErrorMessage().equals(exception.getMessage()));
    }

    @Test
    public void testCreateReprintReceiptsResponse() {
        ReprintReceiptsResponse reprintReceiptsResponse = reprintReceiptsHandler.createReprintReceiptsResponse("1");

        assertNotNull(reprintReceiptsResponse);
        assertNotNull(reprintReceiptsResponse.getReprintReceiptsResult());
        assertEquals("1", reprintReceiptsResponse.getReprintReceiptsResult().getRequestId());
        assertNull(reprintReceiptsResponse.getReprintReceiptsResult().getExceptionResult());
    }

    @Test
    public void testCreateExceptionReprintReceiptsResponse() {
        ChecOperationException checOperationException = new ChecOperationException(
                ErrorCode.TRANSACTION_RECEIPTS_NOT_AVAILABLE.name(), "No transaction receipts were available.", null);
        ReprintReceiptsResponse reprintReceiptsResponse = reprintReceiptsHandler.createExceptionReprintReceiptsResponse
                (checOperationException, "1");

        assertNotNull(reprintReceiptsResponse);
        assertNotNull(reprintReceiptsResponse.getReprintReceiptsResult());
        assertEquals("1", reprintReceiptsResponse.getReprintReceiptsResult().getRequestId());
        assertNotNull(reprintReceiptsResponse.getReprintReceiptsResult().getExceptionResult());
        assertEquals(ErrorCode.TRANSACTION_RECEIPTS_NOT_AVAILABLE.name(),
                reprintReceiptsResponse.getReprintReceiptsResult().getExceptionResult().getErrorCode());
    }
}