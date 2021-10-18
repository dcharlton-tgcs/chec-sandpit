package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.init.InitializeResponse;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.print.PrinterHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {InitializationHandler.class, ExceptionHandler.class, PrinterHandler.class})
@SpringBootTest(classes = {ConfProperties.class})
public class InitializationHandlerTest {

    @Autowired
    private InitializationHandler initializationHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private IWebService webService;

    @MockBean
    private XmlDocument xmlDocument;

    @MockBean
    private BridgeSession bridgeSession;

    @Before
    public void setUp() {
        initializationHandler.setBridgeSession(bridgeSession);
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.INITIALIZE_REQUEST_ID, "")).thenReturn("11");
    }

    @Captor
    ArgumentCaptor argumentCaptor;

    @Test
    public void testProcessChecMessageGoodInit() throws Exception {
        initializationHandler.processChecMessage(tcpClient, xmlDocument);

        verify(xmlDocument).getOrDefaultNodeTextContent(ChecMessagePath.INITIALIZE_REQUEST_ID, "");
        verify(webService).initializeWebService();
        verify(tcpClient, times(5)).sendResponseToChecApp(argumentCaptor.capture());
        verify(bridgeSession).clearOrder();

        List events = argumentCaptor.getAllValues();

        POSBCStatusEvent event = (POSBCStatusEvent) events.get(0);
        assertEquals(POSBCStatusType.RELEASING_SESSION_RESOURCES.name(), event.getPosbcStatus().getStatus());

        event = (POSBCStatusEvent) events.get(1);
        assertEquals(POSBCStatusType.CONNECTING_TO_POS.name(), event.getPosbcStatus().getStatus());

        event = (POSBCStatusEvent) events.get(2);
        assertEquals(POSBCStatusType.CONNECTED_TO_POS.getValue(), event.getPosbcStatus().getStatusMessage());

        event = (POSBCStatusEvent) events.get(3);
        assertEquals(POSBCStatusType.POS_RESOURCES_INITIALIZED.name(), event.getPosbcStatus().getStatus());

        InitializeResponse response = (InitializeResponse) events.get(4);
        assertEquals("11", response.getInitializeResult().getRequestId());
        assertNull(response.getInitializeResult().getExceptionResult());

    }

    @Test
    public void testInitializeWebServiceFailed() throws Exception {
        doThrow(Exception.class).when(webService).initializeWebService();

        initializationHandler.processChecMessage(tcpClient, xmlDocument);

        verify(xmlDocument).getOrDefaultNodeTextContent(ChecMessagePath.INITIALIZE_REQUEST_ID, "");
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSBCStatusEvent event = (POSBCStatusEvent) events.get(0);
        assertEquals(POSBCStatusType.RELEASING_SESSION_RESOURCES.name(), event.getPosbcStatus().getStatus());

        event = (POSBCStatusEvent) events.get(1);
        assertEquals(POSBCStatusType.CONNECTING_TO_POS.name(), event.getPosbcStatus().getStatus());

        InitializeResponse initializeResponse = (InitializeResponse) events.get(2);
        assertNotNull(initializeResponse.getInitializeResult().getExceptionResult());

    }


}
