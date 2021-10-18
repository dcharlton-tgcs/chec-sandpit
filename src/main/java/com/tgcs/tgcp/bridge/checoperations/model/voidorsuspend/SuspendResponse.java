package com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:SuspendResponse")
public class SuspendResponse implements ResponseType {

    public enum SuspendErrorCode{
        APPLICATION_NOT_IN_PROPER_STATE("APPLICATION_NOT_IN_PROPER_STATE:TRANSACTION_NOT_ACTIVE");

        private final String value;

        SuspendErrorCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "SuspendTransactionResult")
    private SuspendTransactionResult suspendTransactionResult;

    public SuspendResponse() {
    }

    public SuspendResponse(String requestId) {
        this.suspendTransactionResult = new SuspendTransactionResult(requestId);
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    public SuspendTransactionResult getSuspendTransactionResult() {
        return suspendTransactionResult;
    }

    public SuspendResponse setSuspendTransactionResult(SuspendTransactionResult suspendTransactionResult) {
        this.suspendTransactionResult = suspendTransactionResult;
        return this;
    }
}
