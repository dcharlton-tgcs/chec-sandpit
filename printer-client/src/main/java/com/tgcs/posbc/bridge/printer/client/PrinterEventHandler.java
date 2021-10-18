package com.tgcs.posbc.bridge.printer.client;

import com.tgcs.posbc.bridge.printer.model.PrinterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import static com.tgcs.posbc.bridge.printer.client.PrinterClientImpl.executorService;

public class PrinterEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(PrinterEventHandler.class);

    private Socket printerServiceSocket = null;
    private String incomingMessage = null;
    private final LinkedList<PrinterEventListener> listeners = new LinkedList<>();

    protected void startEventListenerServer(Socket printerServiceSocket) {
        this.printerServiceSocket = printerServiceSocket;
        executorService.submit(this::listenOnPrinterServiceForEvents);
    }

    private void sendEventsToListeners() {
        for (PrinterEventListener printerEventListener : listeners) {
            if (printerEventListener != null) {
                try {
                    printerEventListener.onEvent(new PrinterEvent().setEventMessage(incomingMessage));
                } catch (Exception e) {
                    logger.error("Could not send event to listener ", e);
                }
            }
        }
    }

    private void listenOnPrinterServiceForEvents() {
        while (true) {
            boolean dataArrived = readIncomingData();
            if (!dataArrived)
                break;

            logger.debug("Starting processing: " + incomingMessage);
            sendEventsToListeners();
            logger.debug("Processing: ended");
        }
    }

    private boolean readIncomingData() {
        byte[] data = new byte[128 * 1024];

        try {
            int nbrOfBytesReceived = printerServiceSocket.getInputStream().read(data);

            // No data
            if (nbrOfBytesReceived == -1)
                return false;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

        incomingMessage = new String(data, StandardCharsets.UTF_8).trim();
        logger.info("Printer Service Event: \n" + incomingMessage);

        return true;
    }

    public LinkedList<PrinterEventListener> getListeners() {
        return listeners;
    }
}

