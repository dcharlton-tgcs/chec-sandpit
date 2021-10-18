package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Tare {

    /**
     * id (optional)
     */
    @JsonProperty("id")
    private String id = "";

    /**
     * weight
     */
    @JsonProperty("weight")
    private BigDecimal weight;
    /**
     * UOM (optional)
     */
    @JsonProperty("UOM")
    private String UOM = "";

    public String getId() {
        return id;
    }

    public Tare setId(String id) {
        this.id = id;
        return this;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Tare setWeight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public String getUOM() {
        return UOM;
    }

    public Tare setUOM(String UOM) {
        this.UOM = UOM;
        return this;
    }
}
