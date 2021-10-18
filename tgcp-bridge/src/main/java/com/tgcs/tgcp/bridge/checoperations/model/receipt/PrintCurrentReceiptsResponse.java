package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:PrintCurrentReceiptsResponse")
public class PrintCurrentReceiptsResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "PrintCurrentReceiptsResult")
    private PrintCurrentReceiptsResult printCurrentReceiptsResult;

    public PrintCurrentReceiptsResponse() {
    }

    public PrintCurrentReceiptsResponse(PrintCurrentReceiptsResult printCurrentReceiptsResult) {
        this.printCurrentReceiptsResult = printCurrentReceiptsResult;
    }

    public PrintCurrentReceiptsResult getPrintCurrentReceiptsResult() {
        return printCurrentReceiptsResult;
    }

    public PrintCurrentReceiptsResponse setPrintCurrentReceiptsResult(PrintCurrentReceiptsResult printCurrentReceiptsResult) {
        this.printCurrentReceiptsResult = printCurrentReceiptsResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
