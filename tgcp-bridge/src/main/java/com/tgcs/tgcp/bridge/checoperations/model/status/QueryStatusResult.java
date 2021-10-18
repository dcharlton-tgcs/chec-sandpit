package com.tgcs.tgcp.bridge.checoperations.model.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "QueryStatusResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryStatusResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "APIHistory")
    private String apiHistory;

    @XmlElement(name = "POSBCStatus")
    private String posbcStatus;

    @XmlElement(name = "POSBCVersion")
    private POSBCVersion posbcVersion;

    @XmlElement(name = "CurrentDateAndTime")
    private String currentDateAndTime;

    @XmlElement(name = "LastSuccessfulRequestID")
    private String lastSuccessfulRequestID;

    @XmlElement(name = "IsInTransaction")
    private Boolean isInTransaction;

    public QueryStatusResult(){
    }

    public String getRequestId() {
        return requestId;
    }

    public QueryStatusResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getApiHistory() {
        return apiHistory;
    }

    public QueryStatusResult setApiHistory(String apiHistory) {
        this.apiHistory = apiHistory;
        return this;
    }

    public String getPosbcStatus() {
        return posbcStatus;
    }

    public QueryStatusResult setPosbcStatus(String posbcStatus) {
        this.posbcStatus = posbcStatus;
        return this;
    }

    public POSBCVersion getPosbcVersion() {
        return posbcVersion;
    }

    public QueryStatusResult setPosbcVersion(POSBCVersion posbcVersion) {
        this.posbcVersion = posbcVersion;
        return this;
    }

    public String getCurrentDateAndTime() {
        return currentDateAndTime;
    }

    public QueryStatusResult setCurrentDateAndTime(String currentDateAndTime) {
        this.currentDateAndTime = currentDateAndTime;
        return this;
    }

    public String getLastSuccessfulRequestID() {
        return lastSuccessfulRequestID;
    }

    public QueryStatusResult setLastSuccessfulRequestID(String lastSuccessfulRequestID) {
        this.lastSuccessfulRequestID = lastSuccessfulRequestID;
        return this;
    }

    public Boolean getInTransaction() {
        return isInTransaction;
    }

    public QueryStatusResult setInTransaction(Boolean inTransaction) {
        isInTransaction = inTransaction;
        return this;
    }
}
