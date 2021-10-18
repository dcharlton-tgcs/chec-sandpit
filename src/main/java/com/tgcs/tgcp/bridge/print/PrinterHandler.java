package com.tgcs.tgcp.bridge.print;

import com.tgcs.posbc.bridge.printer.client.PrinterClient;
import com.tgcs.posbc.bridge.printer.client.PrinterClientImpl;
import com.tgcs.posbc.bridge.printer.client.PrinterEventListener;
import com.tgcs.posbc.bridge.printer.config.PrinterConfiguration;
import com.tgcs.posbc.bridge.printer.model.PrinterEvent;
import com.tgcs.posbc.bridge.printer.model.Receipt;
import com.tgcs.tgcp.bridge.configuration.PrinterProperties;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.tcpserver.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "pos.printer.handle", havingValue = "true", matchIfMissing = false)
public class PrinterHandler {
    private static final Logger logger = LoggerFactory.getLogger(PrinterHandler.class);

    private PrinterClient printerClient;

    @Autowired(required = false)
    private PrinterProperties printerProperties;

    TcpClient tcpClient = null;

    String incomingMessage = null;

    BridgePrinterEventListener bridgePrinterEventListener = null;

    public void startListenForPrinterEvents(TcpClient tcpClient) {
        if (printerProperties == null) return;
        printerClient = new PrinterClientImpl();
        printerClient.startPrinterClient(getPrinterConfig());
        this.tcpClient = tcpClient;
        bridgePrinterEventListener = new BridgePrinterEventListener(tcpClient);
        printerClient.addEventListener(bridgePrinterEventListener);

    }

    private PrinterConfiguration getPrinterConfig() {
        return new PrinterConfiguration()
                .setHost(printerProperties.getHost())
                .setPort(printerProperties.getPort());
    }

    public boolean printReceipt(Receipt receiptData) {
        if (receiptData != null) {
            return this.printerClient.printReceipt(receiptData);
        }
        logger.warn("Null receipt, nothing to print");
        return false;
    }

    public void close(){
        tcpClient = null;
        incomingMessage = null;
        printerClient.removeEventListener(bridgePrinterEventListener);
        bridgePrinterEventListener = null;
        printerClient.closeConnectionIfOpen();
        logger.debug("Printer handling closed");
    }
}

