package com.tgcs.tgcp.bridge.adapter;

import java.math.BigDecimal;
import java.util.Objects;

public class Discount {

    String discountName;
    String discountValue;

    public Discount() {
    }

    public Discount(String discountName, String discountValue) {
        this.discountName = discountName;
        this.discountValue = discountValue;
    }

    public Discount(String discountName, BigDecimal discountValue) {
        this.discountName = discountName;
        this.discountValue = discountValue == null ? "0.00" : discountValue.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    public String getDiscountName() {
        return discountName;
    }

    public Discount setDiscountName(String discountName) {
        this.discountName = discountName;
        return this;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public Discount setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (!(o instanceof Discount)) return false;
        Discount discount = (Discount) o;
        if((discountName == null && discount.discountName != null) ||
                discountValue == null && discount.discountValue != null){
            return false;
        }

        if((discountName != null && discount.discountName == null) ||
                discountValue != null && discount.discountValue == null){
            return false;
        }
        return discountName.trim().equalsIgnoreCase(discount.discountName.trim()) &&
                discountValue.equalsIgnoreCase(discount.discountValue.trim());
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountName, discountValue);
    }
}
