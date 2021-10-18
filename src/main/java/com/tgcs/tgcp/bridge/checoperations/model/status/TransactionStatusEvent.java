package com.tgcs.tgcp.bridge.checoperations.model.status;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:TransactionStatusEvent")
public class TransactionStatusEvent implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatusEvent() {
    }

    public TransactionStatusEvent(String requestId, TransactionStatus transactionStatus) {
        this.requestId = requestId;
        this.transactionStatus = transactionStatus;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.EVENT.toString();
    }

    public String getRequestId() {
        return requestId;
    }

    public TransactionStatusEvent setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public TransactionStatusEvent setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }
}
