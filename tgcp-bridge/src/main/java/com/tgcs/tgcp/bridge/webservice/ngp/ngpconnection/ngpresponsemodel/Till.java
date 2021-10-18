package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import java.util.Date;
import java.util.List;

public class Till {

    Date createTimestamp;

    Date lastModifiedTimestamp;

    Long version;

    String lastModifiedUserId;

    String id;

    String name;

    String type;

    String operatorId;

    String status;

    String warning;

    String businessDayId;

    Date closeTimestamp;

    Date reconcileTimestamp;

    Date completeTimestamp;

    List<TillContentEntry> contents;

    List<TaggedTillContentEntry> otherContents;

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public Till setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public Date getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public Till setLastModifiedTimestamp(Date lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Till setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public Till setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
        return this;
    }

    public String getId() {
        return id;
    }

    public Till setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Till setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Till setType(String type) {
        this.type = type;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public Till setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Till setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getWarning() {
        return warning;
    }

    public Till setWarning(String warning) {
        this.warning = warning;
        return this;
    }

    public String getBusinessDayId() {
        return businessDayId;
    }

    public Till setBusinessDayId(String businessDayId) {
        this.businessDayId = businessDayId;
        return this;
    }

    public Date getCloseTimestamp() {
        return closeTimestamp;
    }

    public Till setCloseTimestamp(Date closeTimestamp) {
        this.closeTimestamp = closeTimestamp;
        return this;
    }

    public Date getReconcileTimestamp() {
        return reconcileTimestamp;
    }

    public Till setReconcileTimestamp(Date reconcileTimestamp) {
        this.reconcileTimestamp = reconcileTimestamp;
        return this;
    }

    public Date getCompleteTimestamp() {
        return completeTimestamp;
    }

    public Till setCompleteTimestamp(Date completeTimestamp) {
        this.completeTimestamp = completeTimestamp;
        return this;
    }

    public List<TillContentEntry> getContents() {
        return contents;
    }

    public Till setContents(List<TillContentEntry> contents) {
        this.contents = contents;
        return this;
    }

    public List<TaggedTillContentEntry> getOtherContents() {
        return otherContents;
    }

    public Till setOtherContents(List<TaggedTillContentEntry> otherContents) {
        this.otherContents = otherContents;
        return this;
    }
}
