package com.tgcs.tgcp.bridge.print;

import com.tgcs.posbc.bridge.printer.client.PrinterEventListener;
import com.tgcs.posbc.bridge.printer.model.PrinterEvent;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BridgePrinterEventListener implements PrinterEventListener {
    private static final Log logger = LogFactory.getLog(BridgePrinterEventListener.class);

    TcpClient tcpClient = null;

    public BridgePrinterEventListener(TcpClient client) {
        this.tcpClient = client;
    }

    @Override
    public void onEvent(PrinterEvent p) {
        logger.debug("Starting processing: " + p.getEventMessage());
        while (tcpClient.getSendingOngoing().get()) {
            //wait until ongoing message was sent
        }
        tcpClient.sendResponseToChecApp(p.getEventMessage());
        logger.debug("Processing: ended");
    }
}
