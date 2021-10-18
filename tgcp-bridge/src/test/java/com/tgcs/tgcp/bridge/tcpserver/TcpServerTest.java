package com.tgcs.tgcp.bridge.tcpserver;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TcpServer.class})
@SpringBootTest(classes = {ConfProperties.class})
public class TcpServerTest {
    @MockBean
    ServerSocket serverSocket;

    @MockBean
    TcpClient tcpClient;

    @MockBean
    Socket socket;

    @Autowired
    TcpServer tcpServer;

    String ipTestValue = "localhost";
    int portTestValue = 8011;

    @SpyBean
    ConfProperties confProperties;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(confProperties, "host", "localhost");
        ReflectionTestUtils.setField(confProperties, "port", 8011);
    }

    @Test
    public void testStartServer() throws IOException {
        tcpServer.start("localhost", 8011);
        verify(serverSocket).bind(any());
    }

    @Test
    public void testStartServerWithConfigValues() throws IOException {
        TcpServer tcpServerSpy = Mockito.spy(tcpServer);
        tcpServerSpy.start();
        verify(tcpServerSpy).start(ipTestValue, portTestValue);
    }

    @Test(expected = IOException.class)
    public void startServerThrowsExceptionTest() throws IOException {
        doThrow(IOException.class).when(serverSocket).bind(any());
        tcpServer.start(ipTestValue, portTestValue);
    }

    @Test(timeout = 300)
    public void testStartListenOnClient() throws IOException, InterruptedException {
        when(serverSocket.accept()).thenReturn(socket).thenReturn(null);
        when(socket.isConnected()).thenReturn(true);
        tcpServer.start(ipTestValue, portTestValue);
        Thread.sleep(100);
        verify(tcpClient).listenOnClient();
    }

    @Test(timeout = 300)
    public void testStartListenOnClientException() throws IOException, InterruptedException {
        when(serverSocket.accept()).thenThrow(IOException.class).thenReturn(null);
        tcpServer.start(ipTestValue, portTestValue);
        Thread.sleep(100);
        verify(tcpClient, times(0)).listenOnClient();
    }

    @Test(timeout = 600)
    public void testStopServer() throws IOException, InterruptedException {
        when(serverSocket.accept()).thenReturn(socket).thenReturn(null);
        when(socket.isConnected()).thenReturn(false);
        tcpServer.start(ipTestValue, portTestValue);
        Thread.sleep(200);
        tcpServer.stop();
        Thread.sleep(200);
        when(socket.isConnected()).thenReturn(true);
        verify(tcpClient, times(0)).listenOnClient();
    }

}