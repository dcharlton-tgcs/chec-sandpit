package com.tgcs.tgcp.bridge.tcpserver;

import com.tgcs.tgcp.bridge.checoperations.OperationsController;
import com.tgcs.tgcp.bridge.common.ObjectToXmlConverter;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Scope("prototype")
public class TcpClient {

    private static final Logger logger = LoggerFactory.getLogger(TcpClient.class);
//    private static Log loggerChecMessage = LogFactory.getLog(LogType.CHEC_MESSAGE);

    @Autowired
    private OperationsController operationsController;

    @Autowired
    XmlDocument xmlMessage;

    private final AtomicBoolean sendingOngoing = new AtomicBoolean(false);

    /**
     * Connected socket client
     */
    Socket socket;

    /**
     * Last messaged received by the server from the client
     */
    String incomingMessage = null;

    public TcpClient(Socket socket) {
        this.socket = socket;
    }

    /**
     * Listen for incoming messages from CHEC
     */
    public void listenOnClient() {
        while (true) {
            try {
                boolean dataArrived = readIncomingData();
                if (!dataArrived)
                    break;

                // enforce check for the "<?" tag in the message, so it's a proper XML
                if (incomingMessage.contains("<?")) {
                    incomingMessage = incomingMessage.substring(incomingMessage.indexOf("<?"));
                }
                logger.debug("Starting processing: " + incomingMessage);
                xmlMessage.loadXml(incomingMessage);
                processIncomingMessage(xmlMessage);
                logger.debug("Processing: ended");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Checks if there are incoming messages, and reads them from stream
     *
     * @return True If a message was received and false otherwise
     * @throws IOException
     */
    private boolean readIncomingData() {
        byte[] data = new byte[128 * 1024];
        final int soepsMessageLength = 4;

        // Read data from connected application (chec)
        try {
            int nbrOfBytesReceived = socket.getInputStream().read(data);

            // No data
            if (nbrOfBytesReceived == -1)
                return false;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

        // Stripping first soepsMessageLength bytes
        data = Arrays.copyOfRange(data, soepsMessageLength, data.length);
        incomingMessage = new String(data, StandardCharsets.UTF_8).trim();
        logger.info("Bridge received: \n" + incomingMessage);

        return true;
    }

    /**
     * Process the message received from the client
     *
     * @param message
     */
    public void processIncomingMessage(XmlDocument message) {
        operationsController.handleMessage(this, message);
    }

    /**
     * Sends a message to Chec application
     *
     * @param message
     */
    public synchronized void sendResponseToChecApp(String message) {
        sendingOngoing.set(true);
        try {
            String xmlResponse = message;
            if (xmlResponse == null || !xmlResponse.contains("<?")) {
                xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xmlResponse;
            }
            logger.info("Bridge sent: \n" + xmlResponse);

            byte[] messageLength = getAs4BytesArray(xmlResponse.getBytes(StandardCharsets.UTF_8).length);

            socket.getOutputStream().write(messageLength);
            socket.getOutputStream().write(xmlResponse.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        sendingOngoing.set(false);
    }

    public byte[] getAs4BytesArray(int value) {
        byte[] messageLength = new byte[4];
        messageLength[0] = (byte) (value >>> 24);
        messageLength[1] = (byte) (value >>> 16);
        messageLength[2] = (byte) (value >>> 8);
        messageLength[3] = (byte) value;
        return messageLength;
    }

    public synchronized void sendResponseToChecApp(Object message) {
        sendResponseToChecApp(ObjectToXmlConverter.jaxbObjectToXML(message, true));
    }

    public AtomicBoolean getSendingOngoing() {
        return sendingOngoing;
    }

}
