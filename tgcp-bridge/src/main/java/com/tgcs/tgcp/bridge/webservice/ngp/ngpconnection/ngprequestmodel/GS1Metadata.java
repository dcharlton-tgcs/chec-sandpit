package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

public class GS1Metadata {

    String applySaveValueTo;

    GS1Item primaryItem;

    String saveValueType;

    /**
     * additionalPurchaseRule (optional)
     */
    String additionalPurchaseRule;

    /**
     * secondaryItem (optional)
     */
    GS1Item secondaryItem;

    /**
     * tertiaryItem (optional)
     */
    GS1Item tertiaryItem;

    /**
     * startDate (optional)
     */
    Long startDate;

    /**
     * expirationDate (optional)
     */
    Long expirationDate;

    /**
     * serialNumber (optional)
     */
    String serialNumber;

    /**
     * retailerCompanyPrefix (optional)
     */
    String retailerCompanyPrefix;

    /**
     * numberOfFreeUnits (optional)
     */
    Long numberOfFreeUnits;

    public String getApplySaveValueTo() {
        return applySaveValueTo;
    }

    public GS1Metadata setApplySaveValueTo(String applySaveValueTo) {
        this.applySaveValueTo = applySaveValueTo;
        return this;
    }

    public GS1Item getPrimaryItem() {
        return primaryItem;
    }

    public GS1Metadata setPrimaryItem(GS1Item primaryItem) {
        this.primaryItem = primaryItem;
        return this;
    }

    public String getSaveValueType() {
        return saveValueType;
    }

    public GS1Metadata setSaveValueType(String saveValueType) {
        this.saveValueType = saveValueType;
        return this;
    }

    public String getAdditionalPurchaseRule() {
        return additionalPurchaseRule;
    }

    public GS1Metadata setAdditionalPurchaseRule(String additionalPurchaseRule) {
        this.additionalPurchaseRule = additionalPurchaseRule;
        return this;
    }

    public GS1Item getSecondaryItem() {
        return secondaryItem;
    }

    public GS1Metadata setSecondaryItem(GS1Item secondaryItem) {
        this.secondaryItem = secondaryItem;
        return this;
    }

    public GS1Item getTertiaryItem() {
        return tertiaryItem;
    }

    public GS1Metadata setTertiaryItem(GS1Item tertiaryItem) {
        this.tertiaryItem = tertiaryItem;
        return this;
    }

    public Long getStartDate() {
        return startDate;
    }

    public GS1Metadata setStartDate(Long startDate) {
        this.startDate = startDate;
        return this;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public GS1Metadata setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public GS1Metadata setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public String getRetailerCompanyPrefix() {
        return retailerCompanyPrefix;
    }

    public GS1Metadata setRetailerCompanyPrefix(String retailerCompanyPrefix) {
        this.retailerCompanyPrefix = retailerCompanyPrefix;
        return this;
    }

    public Long getNumberOfFreeUnits() {
        return numberOfFreeUnits;
    }

    public GS1Metadata setNumberOfFreeUnits(Long numberOfFreeUnits) {
        this.numberOfFreeUnits = numberOfFreeUnits;
        return this;
    }
}
