package com.tgcs.tgcp.bridge.checoperations.model.addcoupon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CouponItem")
public class CouponItem {

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "ReturnFlag")
    private boolean returnFlag;

    @XmlElement(name = "DepositFlag")
    private boolean depositFlag;

    @XmlElement(name = "VoidFlag")
    private boolean voidFlag;

    @XmlElement(name = "ItemIdentifier")
    private String itemIdentifier;

    @XmlElement(name = "RegularUnitPrice")
    private String regularUnitPrice;

    @XmlElement(name = "Quantity")
    private String quantity;

    @XmlElement(name = "Value")
    private String value;

    @XmlElement(name = "DealPrice")
    private String dealPrice;

    @XmlElement(name = "DealQuantity")
    private String dealQuantity;

    @XmlElement(name = "DepartmentNumber")
    private String departmentNumber;

    @XmlElement(name = "RestrictedAge")
    private String restrictedAge = "0";

    @XmlElement(name = "ReducesFoodstampBalanceDueFlag")
    private boolean reducesFoodstampBalanceDueFlag;

    @XmlElement(name = "ReducesTaxDueFlag")
    private boolean reducesTaxDueFlag;

    @XmlElement(name = "ItemRepeatAllowedFlag")
    private boolean itemRepeatAllowedFlag;

    @XmlElement(name = "ManufacturerNumber")
    private String manufacturerNumber;

    public String getDescription() {
        return description;
    }

    public CouponItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public CouponItem setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        return this;
    }

    public boolean isDepositFlag() {
        return depositFlag;
    }

    public CouponItem setDepositFlag(boolean depositFlag) {
        this.depositFlag = depositFlag;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public CouponItem setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public CouponItem setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
        return this;
    }

    public String getRegularUnitPrice() {
        return regularUnitPrice;
    }

    public CouponItem setRegularUnitPrice(String regularUnitPrice) {
        this.regularUnitPrice = regularUnitPrice;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public CouponItem setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CouponItem setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public CouponItem setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
        return this;
    }

    public String getDealQuantity() {
        return dealQuantity;
    }

    public CouponItem setDealQuantity(String dealQuantity) {
        this.dealQuantity = dealQuantity;
        return this;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public CouponItem setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
        return this;
    }

    public String getRestrictedAge() {
        return restrictedAge;
    }

    public CouponItem setRestrictedAge(String restrictedAge) {
        this.restrictedAge = restrictedAge;
        return this;
    }

    public boolean isReducesFoodstampBalanceDueFlag() {
        return reducesFoodstampBalanceDueFlag;
    }

    public CouponItem setReducesFoodstampBalanceDueFlag(boolean reducesFoodstampBalanceDueFlag) {
        this.reducesFoodstampBalanceDueFlag = reducesFoodstampBalanceDueFlag;
        return this;
    }

    public boolean isReducesTaxDueFlag() {
        return reducesTaxDueFlag;
    }

    public CouponItem setReducesTaxDueFlag(boolean reducesTaxDueFlag) {
        this.reducesTaxDueFlag = reducesTaxDueFlag;
        return this;
    }

    public boolean isItemRepeatAllowedFlag() {
        return itemRepeatAllowedFlag;
    }

    public CouponItem setItemRepeatAllowedFlag(boolean itemRepeatAllowedFlag) {
        this.itemRepeatAllowedFlag = itemRepeatAllowedFlag;
        return this;
    }

    public String getManufacturerNumber() {
        return manufacturerNumber;
    }

    public CouponItem setManufacturerNumber(String manufacturerNumber) {
        this.manufacturerNumber = manufacturerNumber;
        return this;
    }
}
