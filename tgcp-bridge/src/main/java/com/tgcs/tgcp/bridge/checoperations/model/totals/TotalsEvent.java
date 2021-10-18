package com.tgcs.tgcp.bridge.checoperations.model.totals;

import com.tgcs.tgcp.bridge.checoperations.model.ParameterExtension;
import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:TotalsEvent")
public class TotalsEvent implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "ParameterExtension")
    private ParameterExtension parameterExtension;

    @XmlElement(name = "TransactionTotals")
    private TransactionTotals transactionTotals;

    @Override
    public String getMessageType() {
        return ResponseTypeValues.EVENT.getValue();
    }

    public String getRequestId() {
        return requestId;
    }

    public TotalsEvent setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ParameterExtension getParameterExtension() {
        return parameterExtension;
    }

    public void setParameterExtension(ParameterExtension parameterExtension) {
        this.parameterExtension = parameterExtension;
    }

    public TransactionTotals getTransactionTotals() {
        return transactionTotals;
    }

    public TotalsEvent setTransactionTotals(TransactionTotals transactionTotals) {
        this.transactionTotals = transactionTotals;
        return this;
    }
}
