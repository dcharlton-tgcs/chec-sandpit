package com.tgcs.tgcp.bridge.checoperations.model.status;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:POSBCStatusEvent")
public class POSBCStatusEvent implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "POSBCStatus")
    private POSBCStatus posbcStatus;

    @XmlElement(name = "PrinterStatus")
    private PrinterStatus printerStatus;

    private POSBCStatusEvent() {}

    public static POSBCStatusEvent createPOSBCStatus(String severity, String status, String message) {
        POSBCStatusEvent event = new POSBCStatusEvent();
        event.posbcStatus = new POSBCStatus(severity, status, message);
        return event;
    }

    public static POSBCStatusEvent createPrinterStatus(String severity, String status, String message) {
        POSBCStatusEvent event = new POSBCStatusEvent();
        event.printerStatus = new PrinterStatus(severity, status, message);
        return event;
    }

    public POSBCStatus getPosbcStatus() {
        return posbcStatus;
    }

    public PrinterStatus getPrinterStatus() {
        return printerStatus;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.EVENT.getValue();
    }
}
