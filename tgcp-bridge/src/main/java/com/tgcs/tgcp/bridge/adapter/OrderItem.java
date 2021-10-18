package com.tgcs.tgcp.bridge.adapter;

import java.util.ArrayList;
import java.util.List;

public class OrderItem extends Item {

    private String parentOrderItemId;
    private List<ChildItem> childItemList;
    private String itemEntryMethod;
    private List<Discount> discounts = new ArrayList<>();

    /**
     * utility flag to check if item is sold by weight
     */
    private boolean isWeightedItem;

    /**
     * This field will contain the unit of measure. If it's not EACH, it will be used for weighted items receipt lines
     */
    private String uom;

    private boolean foodStampEligibleFlag;

    private boolean itemRepeatAllowedFlag = true;

    private boolean taxableFlag;

    private boolean notForSale;

    private boolean isNotOnFileItem;

    private String parentIdentificationNumber;

    public String getParentOrderItemId() {
        return parentOrderItemId;
    }

    public OrderItem setParentOrderItemId(String parentOrderItemId) {
        this.parentOrderItemId = parentOrderItemId;
        return this;
    }

    public List<ChildItem> getChildItemList() {
        return childItemList;
    }

    public OrderItem setChildItemList(List<ChildItem> childItemList) {
        this.childItemList = childItemList;
        return this;
    }

    public String getItemEntryMethod() {
        return itemEntryMethod;
    }

    public OrderItem setItemEntryMethod(String itemEntryMethod) {
        this.itemEntryMethod = itemEntryMethod;
        return this;
    }

    public boolean isWeightedItem() {
        return isWeightedItem;
    }

    public OrderItem setWeightedItem(boolean weightedItem) {
        isWeightedItem = weightedItem;
        return this;
    }

    public String getUom() {
        return uom;
    }

    public OrderItem setUom(String uom) {
        this.uom = uom;
        return this;
    }

    public boolean isFoodStampEligibleFlag() {
        return foodStampEligibleFlag;
    }

    public OrderItem setFoodStampEligibleFlag(boolean foodStampEligibleFlag) {
        this.foodStampEligibleFlag = foodStampEligibleFlag;
        return this;
    }

    public boolean isItemRepeatAllowedFlag() {
        return itemRepeatAllowedFlag;
    }

    public OrderItem setItemRepeatAllowedFlag(boolean itemRepeatAllowedFlag) {
        this.itemRepeatAllowedFlag = itemRepeatAllowedFlag;
        return this;
    }

    public boolean isTaxableFlag() {
        return taxableFlag;
    }

    public OrderItem setTaxableFlag(boolean taxableFlag) {
        this.taxableFlag = taxableFlag;
        return this;
    }

    public boolean isNotForSale() {
        return notForSale;
    }

    public OrderItem setNotForSale(boolean notForSale) {
        this.notForSale = notForSale;
        return this;
    }

    public boolean isNotOnFileItem() {
        return isNotOnFileItem;
    }

    public OrderItem setNotOnFileItem(boolean notOnFileItem) {
        isNotOnFileItem = notOnFileItem;
        return this;
    }

    public boolean hasLinkedItems() {
        return !getChildItemList().isEmpty();
    }

    public String getParentIdentificationNumber() {
        return parentIdentificationNumber;
    }

    public OrderItem setParentIdentificationNumber(String parentIdentificationNumber) {
        this.parentIdentificationNumber = parentIdentificationNumber;
        return this;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
