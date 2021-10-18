package com.tgcs.tgcp.bridge.tcpserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StartTcpServer.class})
public class StartTcpServerTest {

    @Autowired
    StartTcpServer autoStartTcpServer;

    @MockBean
    TcpServer tcpServer;

    @Test
    public void testServerStart() throws IOException {
        autoStartTcpServer.onStartup(null);
        verify(tcpServer).start();
    }


}