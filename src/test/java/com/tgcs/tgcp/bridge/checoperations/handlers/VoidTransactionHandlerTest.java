package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend.VoidTransactionResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {VoidTransactionHandler.class})
public class VoidTransactionHandlerTest {

    @Autowired
    private VoidTransactionHandler voidTransactionHandler;

    @MockBean
    private XmlDocument xmlDocument;

    @MockBean
    private BridgeSession bridgeSession;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private IWebService webService;

    @Captor
    private ArgumentCaptor argumentCaptor;

    @MockBean
    private OrderAdapter orderAdapter;


    @Before
    public void setUp() {
        voidTransactionHandler.setBridgeSession(bridgeSession);
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.VOID_TRANSACTION_REQUEST_ID, "")).thenReturn("11");
    }

    @Test
    public void testProcessChecMessage() throws Exception {
        when(bridgeSession.getOrderAdapter()).thenReturn(orderAdapter);
        when(orderAdapter.getOrderId()).thenReturn("1");

        voidTransactionHandler.processChecMessage(tcpClient, xmlDocument);

        verify(xmlDocument).getOrDefaultNodeTextContent(ChecMessagePath.VOID_TRANSACTION_REQUEST_ID, "");
        verify(webService).voidTransaction();
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());
        verify(bridgeSession).clearOrder();

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_VOID.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEvent = (POSReceiptEvent) events.get(1);
        assertEquals("11", posReceiptEvent.getRequestId());

        VoidTransactionResponse voidTransactionResponse = (VoidTransactionResponse) events.get(2);
        assertNotNull(voidTransactionResponse.getVoidTransactionResult());
        assertEquals("11", voidTransactionResponse.getVoidTransactionResult().getRequestId());
        assertNull(voidTransactionResponse.getVoidTransactionResult().getExceptionResult());
    }

    @Test
    public void testVoidTransactionFailed() throws Exception {
        doThrow(Exception.class).when(webService).voidTransaction();

        voidTransactionHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        VoidTransactionResponse voidTransactionResponse = (VoidTransactionResponse) argumentCaptor.getValue();
        assertEquals("11", voidTransactionResponse.getVoidTransactionResult().getRequestId());
        assertNotNull(voidTransactionResponse.getVoidTransactionResult().getExceptionResult());
    }

}