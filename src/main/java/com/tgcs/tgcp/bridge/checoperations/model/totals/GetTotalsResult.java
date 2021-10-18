package com.tgcs.tgcp.bridge.checoperations.model.totals;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GetTotalsResult")
public class GetTotalsResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "TransactionTotals")
    TransactionTotals transactionTotals;

    public GetTotalsResult() {
    }

    public GetTotalsResult(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public GetTotalsResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public TransactionTotals getTransactionTotals() {
        return transactionTotals;
    }

    public GetTotalsResult setTransactionTotals(TransactionTotals transactionTotals) {
        this.transactionTotals = transactionTotals;
        return this;
    }
}
