package com.tgcs.tgcp.bridge.checoperations.model.totals;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TransactionTotals")
public class TransactionTotals {

    @XmlElement(name = "Total")
    private String total;

    @XmlElement(name = "SubTotal")
    private String subtotal;

    @XmlElement(name = "Tax")
    private String tax;

    @XmlElement(name = "BalanceDue")
    private String balanceDue;

    @XmlElement(name = "ChangeDue")
    private String changeDue;

    @XmlElement(name = "FoodstampChangeDue")
    private String FoodstampChangeDue = "0.00";

    @XmlElement(name = "FoodstampTotal")
    private String foodstampTotal = "0.00";

    @XmlElement(name = "FoodstampBalanceDue")
    private String foodStampBalanceDue = "0.00";

    @XmlElement(name = "CouponTotal")
    private String couponTotal;

    @XmlElement(name = "TotalItems")
    private String totalItems;

    @XmlElement(name = "TotalCoupons")
    private String totalCoupons;

    @XmlElement(name = "TotalSavings")
    private String totalSavings;

    @XmlElement(name = "TenderApplied")
    private String tenderApplied;

    public String getTotal() {
        return total;
    }

    public TransactionTotals setTotal(String total) {
        this.total = total;
        return this;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public TransactionTotals setSubtotal(String subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public String getTax() {
        return tax;
    }

    public TransactionTotals setTax(String tax) {
        this.tax = tax;
        return this;
    }

    public String getBalanceDue() {
        return balanceDue;
    }

    public TransactionTotals setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
        return this;
    }

    public String getChangeDue() {
        return changeDue;
    }

    public TransactionTotals setChangeDue(String changeDue) {
        this.changeDue = changeDue;
        return this;
    }

    public String getFoodstampChangeDue() {
        return FoodstampChangeDue;
    }

    public TransactionTotals setFoodstampChangeDue(String foodstampChangeDue) {
        FoodstampChangeDue = foodstampChangeDue;
        return this;
    }

    public String getFoodstampTotal() {
        return foodstampTotal;
    }

    public TransactionTotals setFoodstampTotal(String foodstampTotal) {
        this.foodstampTotal = foodstampTotal;
        return this;
    }

    public String getFoodStampBalanceDue() {
        return foodStampBalanceDue;
    }

    public TransactionTotals setFoodStampBalanceDue(String foodStampBalanceDue) {
        this.foodStampBalanceDue = foodStampBalanceDue;
        return this;
    }

    public String getCouponTotal() {
        return couponTotal;
    }

    public TransactionTotals setCouponTotal(String couponTotal) {
        this.couponTotal = couponTotal;
        return this;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public TransactionTotals setTotalItems(String totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public String getTotalCoupons() {
        return totalCoupons;
    }

    public TransactionTotals setTotalCoupons(String totalCoupons) {
        this.totalCoupons = totalCoupons;
        return this;
    }

    public String getTotalSavings() {
        return totalSavings;
    }

    public TransactionTotals setTotalSavings(String totalSavings) {
        this.totalSavings = totalSavings;
        return this;
    }

    public String getTenderApplied() {
        return tenderApplied;
    }

    public TransactionTotals setTenderApplied(String tenderApplied) {
        this.tenderApplied = tenderApplied;
        return this;
    }
}
