package com.tgcs.tgcp.bridge.adapter;

import java.math.BigDecimal;

public class Item implements ChecAdapter {

    private String id;
    private String description;
    private boolean returnFlag;
    private boolean voidFlag;
    private String itemIdentifier;
    private BigDecimal extendedPrice;
    private BigDecimal quantity;
    private String departmentNumber;
    private int restrictedAge = 0;
    private String scanDataType;

    public String getId() {
        return id;
    }

    public Item setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public Item setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public Item setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public Item setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
        return this;
    }

    public BigDecimal getExtendedPrice() {
        return extendedPrice;
    }

    public Item setExtendedPrice(BigDecimal extendedPrice) {
        this.extendedPrice = extendedPrice;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Item setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public Item setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
        return this;
    }

    public int getRestrictedAge() {
        return restrictedAge;
    }

    public Item setRestrictedAge(int restrictedAge) {
        this.restrictedAge = restrictedAge;
        return this;
    }

    public String getScanDataType() {
        return scanDataType;
    }

    public Item setScanDataType(String scanDataType) {
        this.scanDataType = scanDataType;
        return this;
    }
}
