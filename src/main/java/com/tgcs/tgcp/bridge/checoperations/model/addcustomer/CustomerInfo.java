package com.tgcs.tgcp.bridge.checoperations.model.addcustomer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CustomerInfo")
public class CustomerInfo {

    @XmlElement(name = "CustomerID")
    private String customerId;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "YTDPoints")
    private String ytdPoints;


    public String getCustomerId() {
        return customerId;
    }

    public CustomerInfo setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomerInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getYtdPoints() {
        return ytdPoints;
    }

    public CustomerInfo setYtdPoints(String ytdPoints) {
        this.ytdPoints = ytdPoints;
        return this;
    }
}
