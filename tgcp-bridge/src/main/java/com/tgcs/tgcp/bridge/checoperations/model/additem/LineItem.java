package com.tgcs.tgcp.bridge.checoperations.model.additem;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LineItem")
public class LineItem {

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

    @XmlElement(name = "ItemEntryMethod")
    private String itemEntryMethod;

    @XmlElement(name = "RegularUnitPrice")
    private String regularUnitPrice;

    @XmlElement(name = "Quantity")
    private String quantity;

    @XmlElement(name = "ExtendedPrice")
    private String extendedPrice;

    @XmlElement(name = "Weight")
    private String weight;

    @XmlElement(name = "DealPrice")
    private String dealPrice;

    @XmlElement(name = "DealQuantity")
    private String dealQuantity;

    @XmlElement(name = "DepartmentNumber")
    private String departmentNumber;

    @XmlElement(name = "MixAndMatchPricingGroup")
    private String mixAndMatchPricingGroup;

    @XmlElement(name = "PriceDerivationMethod")
    private String priceDerivationMethod ;

    @XmlElement(name = "TimeRestrictedFlag")
    private boolean timeRestrictedFlag;

    @XmlElement(name = "RestrictedAge")
    private String restrictedAge = "0";

    @XmlElement(name = "FoodStampEligibleFlag")
    private boolean foodStampEligibleFlag;

    @XmlElement(name = "WICEligibleFlag")
    private boolean wicEligibleFlag;

    @XmlElement(name = "ItemRepeatAllowedFlag")
    private boolean itemRepeatAllowedFlag = true;

    @XmlElement(name = "TaxableFlag")
    private boolean taxableFlag;


    public LineItem() {
    }

    public String getDescription() {
        return description;
    }

    public LineItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public LineItem setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        return this;
    }

    public boolean isDepositFlag() {
        return depositFlag;
    }

    public LineItem setDepositFlag(boolean depositFlag) {
        this.depositFlag = depositFlag;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public LineItem setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public LineItem setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
        return this;
    }

    public String getItemEntryMethod() {
        return itemEntryMethod;
    }

    public LineItem setItemEntryMethod(String itemEntryMethod) {
        this.itemEntryMethod = itemEntryMethod;
        return this;
    }

    public String getRegularUnitPrice() {
        return regularUnitPrice;
    }

    public LineItem setRegularUnitPrice(String regularUnitPrice) {
        this.regularUnitPrice = regularUnitPrice;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public LineItem setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getExtendedPrice() {
        return extendedPrice;
    }

    public LineItem setExtendedPrice(String extendedPrice) {
        this.extendedPrice = extendedPrice;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public LineItem setWeight(String weight) {
        this.weight = weight;
        return this;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public LineItem setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
        return this;
    }

    public String getDealQuantity() {
        return dealQuantity;
    }

    public LineItem setDealQuantity(String dealQuantity) {
        this.dealQuantity = dealQuantity;
        return this;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public LineItem setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
        return this;
    }

    public String getMixAndMatchPricingGroup() {
        return mixAndMatchPricingGroup;
    }

    public LineItem setMixAndMatchPricingGroup(String mixAndMatchPricingGroup) {
        this.mixAndMatchPricingGroup = mixAndMatchPricingGroup;
        return this;
    }

    public String getPriceDerivationMethod() {
        return priceDerivationMethod;
    }

    public LineItem setPriceDerivationMethod(String priceDerivationMethod) {
        this.priceDerivationMethod = priceDerivationMethod;
        return this;
    }

    public boolean isTimeRestrictedFlag() {
        return timeRestrictedFlag;
    }

    public LineItem setTimeRestrictedFlag(boolean timeRestrictedFlag) {
        this.timeRestrictedFlag = timeRestrictedFlag;
        return this;
    }

    public String getRestrictedAge() {
        return restrictedAge;
    }

    public LineItem setRestrictedAge(String restrictedAge) {
        this.restrictedAge = restrictedAge;
        return this;
    }

    public boolean isFoodStampEligibleFlag() {
        return foodStampEligibleFlag;
    }

    public LineItem setFoodStampEligibleFlag(boolean foodStampEligibleFlag) {
        this.foodStampEligibleFlag = foodStampEligibleFlag;
        return this;
    }

    public boolean isWicEligibleFlag() {
        return wicEligibleFlag;
    }

    public LineItem setWicEligibleFlag(boolean wicEligibleFlag) {
        this.wicEligibleFlag = wicEligibleFlag;
        return this;
    }

    public boolean isItemRepeatAllowedFlag() {
        return itemRepeatAllowedFlag;
    }

    public LineItem setItemRepeatAllowedFlag(boolean itemRepeatAllowedFlag) {
        this.itemRepeatAllowedFlag = itemRepeatAllowedFlag;
        return this;
    }

    public boolean isTaxableFlag() {
        return taxableFlag;
    }

    public LineItem setTaxableFlag(boolean taxableFlag) {
        this.taxableFlag = taxableFlag;
        return this;
    }
}
