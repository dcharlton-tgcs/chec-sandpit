package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;

public class GS1Item {

    BigDecimal purchaseRequirement;

    BigDecimal purchaseRequirementType;

    String familyCode;

    String companyPrefix;

    public BigDecimal getPurchaseRequirement() {
        return purchaseRequirement;
    }

    public GS1Item setPurchaseRequirement(BigDecimal purchaseRequirement) {
        this.purchaseRequirement = purchaseRequirement;
        return this;
    }

    public BigDecimal getPurchaseRequirementType() {
        return purchaseRequirementType;
    }

    public GS1Item setPurchaseRequirementType(BigDecimal purchaseRequirementType) {
        this.purchaseRequirementType = purchaseRequirementType;
        return this;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public GS1Item setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
        return this;
    }

    public String getCompanyPrefix() {
        return companyPrefix;
    }

    public GS1Item setCompanyPrefix(String companyPrefix) {
        this.companyPrefix = companyPrefix;
        return this;
    }
}
