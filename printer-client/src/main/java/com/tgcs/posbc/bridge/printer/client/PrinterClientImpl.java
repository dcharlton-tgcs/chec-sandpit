package com.tgcs.posbc.bridge.printer.client;

import com.tgcs.posbc.bridge.printer.config.PrinterConfiguration;
import com.tgcs.posbc.bridge.printer.model.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class PrinterClientImpl implements PrinterClient {
    private static final int RETRY_NBR = 5;
    private static final Logger logger = LoggerFactory.getLogger(PrinterClientImpl.class);
    protected static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private PrinterConfiguration printerConfig;
    private Socket printerServiceSocket = null;
    private PrinterEventHandler printerEventHandler = null;

    @Override
    public void startPrinterClient(PrinterConfiguration printerConfig) {
        this.printerConfig = printerConfig;
        if (this.printerConfig == null || !connectToPrinter(this.printerConfig.getHost(), printerConfig.getPort())) return;
        initEventHandler();
    }

    protected void initEventHandler() {
        printerEventHandler = new PrinterEventHandler();
        printerEventHandler.startEventListenerServer(printerServiceSocket);
    }

    protected boolean connectToPrinter(String host, int port) {
        int retry = 0;
        boolean connected = false;
        while (!connected)
            try {
                closeConnectionIfOpen();
                printerServiceSocket = new Socket(host, port);
                connected = true;
            } catch (Exception e) {
                retry++;
                if (retry < RETRY_NBR) {
                    logger.error("Failed to connect to the printer service " + e.getMessage() + ", retrying... " + retry);
                    continue;
                }
                logger.error("Failed to connect to the printer service, please check if the printer service is running ", e);
                logger.error("Host " + host + ", port " + port);
                return false;
            }
        return true;
    }

    public void closeConnectionIfOpen() {
        if (printerServiceSocket == null) return;
        try {
            printerServiceSocket.close();
            printerServiceSocket = null;
        } catch (IOException e) {
            logger.debug("Close Connection to printer service failed", e);
        }
    }

    public boolean sendMessageToPrinter(String message) {
        try {
            logger.info("Sending message to printer service: \n" + message);

            printerServiceSocket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
            printerServiceSocket.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Failed to send message to printer service", e);
            return false;
        }
        return true;
    }

    public boolean printReceipt(Receipt receiptData) {
        return sendMessageToPrinter(toXml(receiptData));
    }

    @Override
    public void addEventListener(PrinterEventListener listener) {
        if (printerEventHandler != null) {
            printerEventHandler.getListeners().add(listener);
        }
    }

    @Override
    public void removeEventListener(PrinterEventListener listener) {
        if (printerEventHandler != null) {
            printerEventHandler.getListeners().remove(listener);
        }
    }

    public static String toXml(Object object) {
        if (object == null) object = "";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller(); // Create Marshaller
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Required formatting?
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(object, sw);
            String xml = sw.toString();

            if (xml == null || !xml.startsWith("<?")) {
                xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml;
            }


            return xml;
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
