package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.status.ReportStatusEventsResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.springframework.stereotype.Component;

@Component
public class ReportStatusHandler implements IHandler, ChecMessagePath {
    private BridgeSession bridgeSession;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        //Send printer status event
        client.sendResponseToChecApp(POSBCStatusEvent.createPrinterStatus(POSBCStatusType.SEVERITY_INFO.getValue(),
                                                                          POSBCStatusType.INITIALIZED.name(),
                                                                          POSBCStatusType.INITIALIZED.getValue()));

        //Send report status response
        client.sendResponseToChecApp(creteReportStatusEventsResponse(message));
    }

    private ReportStatusEventsResponse creteReportStatusEventsResponse(XmlDocument message) {
        return new ReportStatusEventsResponse(message.getSilentNodeTextContent(REPORT_STATUS_EVENTS_REQUEST_ID));
    }

}
