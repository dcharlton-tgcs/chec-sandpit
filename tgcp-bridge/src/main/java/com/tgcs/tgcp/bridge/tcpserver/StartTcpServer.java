package com.tgcs.tgcp.bridge.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartTcpServer implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LoggerFactory.getLogger(StartTcpServer.class);

    @Autowired
    private TcpServer tcpServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Trying to start POSBC Service");
    }

    @EventListener
    public void onStartup(ApplicationReadyEvent event) {
        try {
            tcpServer.start();
            logger.info("*** Bridge has started! ***");
        } catch (IOException e) {
            logger.error("Failed to start server", e);
            System.exit(2);
        }
    }

    /**
     * This event will be fired only if the shutdown is issued via a forceful close of the terminal window.
     * Closing lane or OS shutdown (à la "kill -9") won't fire this event
     *
     * @param event
     */
    @EventListener
    public void onShutdown(ContextClosedEvent event) {
        logger.info("*** Bridge has been closed! ***");
    }


}
