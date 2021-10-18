package com.tgcs.tgcp.bridge.tcpserver;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TcpServer implements IServer, DisposableBean {
    private static Logger logger = LoggerFactory.getLogger(TcpServer.class);
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    ConfProperties properties;

    @Autowired
    ApplicationContext context;

    @Autowired
    private ServerSocket serverSocket;

    private List<TcpClient> connectedClients = new ArrayList<>();

    private final AtomicBoolean serverRunning = new AtomicBoolean(false);

    @Override
    public void start() throws IOException {
        start(this.properties.getHost(), this.properties.getPort());
    }

    @Override
    public void start(String ip, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        serverSocket.bind(address);
        serverRunning.set(true);
        executorService.submit(this::acceptConnections);
    }

    private void acceptConnections() {
        while (serverRunning.get()) {
            try {
                Socket socket = serverSocket.accept();
                if (socket.isConnected()) {
                    TcpClient connectedClient = context.getBean(TcpClient.class, socket);
                    connectedClients.add(connectedClient);
                    connectedClient.listenOnClient();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        serverRunning.set(false);
        destroy();
    }

    @Override
    public void destroy() {
        try {
            logger.debug("Tcp Server stopping");
            serverRunning.set(false);
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            logger.error("Error occurred during server closure", e);
        }

    }


}
