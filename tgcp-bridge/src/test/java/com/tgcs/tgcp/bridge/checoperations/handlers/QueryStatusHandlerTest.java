package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.status.QueryStatusResponse;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {QueryStatusHandler.class, QueryStatusResponse.class, TcpClient.class})
public class QueryStatusHandlerTest {

    @Autowired
    private QueryStatusHandler queryStatusHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private XmlDocument xmlMessage;

    @MockBean
    private BridgeSession bridgeSession;

    @Before
    public void setUp() {
        queryStatusHandler.setBridgeSession(bridgeSession);
    }

    @Captor
    ArgumentCaptor argumentCaptor;

    @Test
    public void sendProcessChecMessageSendsQueryResponseTest() {
        when(xmlMessage.getOrDefaultNodeTextContent(anyString(), anyString())).thenReturn("10");
        when(bridgeSession.getNGPStatus()).thenReturn(TransactionStatusType.TRANSACTION_START.toString());

        queryStatusHandler.processChecMessage(tcpClient, xmlMessage);
        verify(tcpClient, times(1)).sendResponseToChecApp(argumentCaptor.capture());

        assertEquals("10", ((QueryStatusResponse) argumentCaptor.getValue()).getQueryStatusResult().getRequestId());
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(),
                ((QueryStatusResponse) argumentCaptor.getValue()).getQueryStatusResult().getPosbcStatus());

    }

    @Test
    public void sendProcessChecMessageNoRequestIdTest() {
        when(xmlMessage.getOrDefaultNodeTextContent(anyString(), anyString())).thenReturn("");
        when(bridgeSession.getNGPStatus()).thenReturn(TransactionStatusType.TRANSACTION_START.toString());

        queryStatusHandler.processChecMessage(tcpClient, xmlMessage);
        verify(tcpClient, times(1)).sendResponseToChecApp(argumentCaptor.capture());

        assertNull(((QueryStatusResponse) argumentCaptor.getValue()).getQueryStatusResult().getRequestId());
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(),
                ((QueryStatusResponse) argumentCaptor.getValue()).getQueryStatusResult().getPosbcStatus());

    }


}