package com.tgcs.tgcp.bridge.tcpserver;

import com.tgcs.tgcp.bridge.checoperations.OperationsController;
import com.tgcs.tgcp.bridge.checoperations.model.init.InitializeResponse;
import com.tgcs.tgcp.bridge.common.ObjectToXmlConverter;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TcpClient.class})
public class TcpClientTest {

    @MockBean
    XmlDocument xmlDocument;

    @MockBean
    Socket socket;

    @MockBean
    OutputStream outputStream;

    @MockBean
    InputStream inputStream;

    @Autowired
    TcpClient tcpClient;

    @MockBean
    OperationsController operationsController;

    @Before
    public void setUp() throws IOException {
        when(socket.getOutputStream()).thenReturn(outputStream);
        when(socket.getInputStream()).thenReturn(inputStream);
    }

    @Test
    public void processIncomingMessageTest() {
        tcpClient.processIncomingMessage(xmlDocument);
        verify(operationsController, times(1)).handleMessage(tcpClient, xmlDocument);
    }

    @Test
    public void sendObjectResponseToChecTest() throws IOException {
        String toSend = "<abc></abc>";
        tcpClient.sendResponseToChecApp(toSend);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + toSend;
        verify(outputStream, times(1)).write(expected.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void sendResponseToChecAddHeaderTest() throws IOException {
        InitializeResponse initializeResponse = new InitializeResponse("1");
        tcpClient.sendResponseToChecApp(initializeResponse);

        String expectedResult = ObjectToXmlConverter.jaxbObjectToXML(initializeResponse, true);
        int messageLength = expectedResult.getBytes(StandardCharsets.UTF_8).length;
        verify(outputStream, times(1)).write(tcpClient.getAs4BytesArray(messageLength));
        verify(outputStream, times(1)).write(expectedResult.getBytes(StandardCharsets.UTF_8));

    }

    @Test(timeout = 60000)
    public void listenOnClientTest() throws IOException, ParserConfigurationException, SAXException {

        String message = "<abc></abc>";
        byte[] messageLength = new byte[4];
        messageLength[0] = (byte) (message.length() >>> 24);
        messageLength[1] = (byte) (message.length() >>> 16);
        messageLength[2] = (byte) (message.length() >>> 8);
        messageLength[3] = (byte) message.length();

        byte[] request = ArrayUtils.addAll(messageLength, message.getBytes());
        inputStream = new ByteArrayInputStream(request);


        when(socket.getInputStream()).thenReturn(inputStream);
        tcpClient.listenOnClient();
        verify(xmlDocument, times(1)).loadXml("<abc></abc>");
    }

    @Test(timeout = 2000)
    public void listenOnClientBadMessageNotProcessedTest() throws IOException, ParserConfigurationException, SAXException {
        TcpClient tcpClientSpy = Mockito.spy(tcpClient);
        when(inputStream.read(any())).thenReturn(0).thenReturn(-1);
        doThrow(SAXException.class).when(xmlDocument).loadXml(any());
        tcpClientSpy.listenOnClient();
        verify(tcpClientSpy, times(0)).processIncomingMessage(any());
    }

    @Test
    public void readIncomingDataThrowsExceptionTest() throws IOException, ParserConfigurationException, SAXException {
        when(socket.getInputStream()).thenThrow(IOException.class);
        tcpClient.listenOnClient();
        verify(xmlDocument, times(0)).loadXml(any());
    }


}