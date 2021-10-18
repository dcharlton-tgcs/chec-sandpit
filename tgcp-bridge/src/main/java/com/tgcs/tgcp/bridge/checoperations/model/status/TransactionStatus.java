package com.tgcs.tgcp.bridge.checoperations.model.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TransactionStatus")
public class TransactionStatus {

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "ID")
    private String id;

    @XmlElement(name = "Type")
    private String type;

    @XmlElement(name = "Category")
    private String category;

    @XmlElement(name = "Date")
    private String date;

    @XmlElement(name = "Time")
    private String time;

    public String getStatus() {
        return status;
    }

    public TransactionStatus setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getId() {
        return id;
    }

    public TransactionStatus setId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public TransactionStatus setType(String type) {
        this.type = type;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public TransactionStatus setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDate() {
        return date;
    }

    public TransactionStatus setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public TransactionStatus setTime(String time) {
        this.time = time;
        return this;
    }
}
