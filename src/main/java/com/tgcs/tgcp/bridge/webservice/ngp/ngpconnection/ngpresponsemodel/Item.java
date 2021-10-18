package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;

public class Item {
    private String version;

    private LinkedHashMap<String,String> description = new LinkedHashMap<>();

    private LinkedHashMap<String,String> displayName = new LinkedHashMap<>();

    @JsonProperty("UOM")
    private String uom;

    private boolean notForSale;

    private boolean isNotOnFileItem;

    @JsonProperty("isUOMTypeEach")
    private boolean isUOMTypeEach;

    private String skuId;

    public LinkedHashMap<String,String> getDescription() {
        return description;
    }

    public void setDescription(LinkedHashMap<String,String> description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String UOM) {
        this.uom = UOM;
    }

    public boolean getNotForSale() {
        return notForSale;
    }

    public void setNotForSale(boolean notForSale) {
        this.notForSale = notForSale;
    }

    public boolean isUOMTypeEach() {
        return isUOMTypeEach;
    }

    public void setUOMTypeEach(boolean isUOMTypeEach) {
        this.isUOMTypeEach = isUOMTypeEach;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public LinkedHashMap<String,String> getDisplayName() {
        return displayName;
    }

    public Item setDisplayName(LinkedHashMap<String,String> displayName) {
        this.displayName = displayName;
        return this;
    }

    public boolean isNotForSale() {
        return notForSale;
    }

    public boolean isNotOnFileItem() {
        return isNotOnFileItem;
    }

    public Item setNotOnFileItem(boolean notOnFileItem) {
        isNotOnFileItem = notOnFileItem;
        return this;
    }
}
