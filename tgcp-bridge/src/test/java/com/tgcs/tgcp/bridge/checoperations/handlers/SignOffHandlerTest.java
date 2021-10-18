package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.model.close.SignOffResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.print.PrinterHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SignOffHandler.class, ExceptionHandler.class})
@SpringBootTest(classes = {ConfProperties.class})
public class SignOffHandlerTest {

    @Autowired
    private SignOffHandler signOffHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private XmlDocument xmlDocument;

    @Captor
    ArgumentCaptor argumentCaptor;

    @MockBean
    private IWebService webService;

    @MockBean
    private PrinterHandler printerHandler;

    @SpyBean
    ConfProperties confProperties;


    @Test
    public void testSignOffNoPrinter() {
        when(xmlDocument.getSilentNodeTextContent(anyString())).thenReturn("11");
        ReflectionTestUtils.setField(confProperties, "posHandlePrinter", false);

        signOffHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).releaseResources();

        assertEquals("11", ((SignOffResponse) argumentCaptor.getValue()).getSignOffResult().getRequestId());
    }

    @Test
    public void testSignOffWithPrinter() {
        when(xmlDocument.getSilentNodeTextContent(anyString())).thenReturn("11");
        ReflectionTestUtils.setField(confProperties, "posHandlePrinter", true);

        signOffHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).releaseResources();

        assertEquals("11", ((SignOffResponse) argumentCaptor.getValue()).getSignOffResult().getRequestId());
    }

}