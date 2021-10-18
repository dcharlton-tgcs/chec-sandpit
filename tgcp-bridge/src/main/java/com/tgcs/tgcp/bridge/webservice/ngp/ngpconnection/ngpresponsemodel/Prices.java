package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

public class Prices {
 /*                       "total": 0.75,
                                "id": "5e30602830c49e00012cec01",
                                "subTotal": 0.75,
                                "quantity": 1,
                                "salePrice": 0.75,
                                "salePriceLocked": false,
                                "totalPriceModifications": 0.00,
                                "totalBeforeModifications": 0.75,
                                "totalBeforeTaxes": 0.71,
                                "totalEffectiveTaxRate": 5.00,
                                "totalTaxableAmount": 0.75,
                                "totalTaxes": 0.04,
                                "taxes": [
    {
        "id": "5e3060b630c49e00012cec0b",
            "rateType": "PERCENT",
            "type": "VAT",
            "aggregateIds": [
        "5e3060b630c49e00012cec0a"
                            ],
        "indicator": "L",
            "rate": 5,
            "taxableAmount": 0.75,
            "amount": 0.03571,
            "amountWithTax": 0.75000,
            "name": "VAT Low",
            "taxGroupId": "LOW",
            "taxSetId": "UK"
    }
                    ],
                            "listPrice": {
        "currencyCode": "GBP",
                "originalValue": 0.7500,
                "currencyValue": {
            "currencyCode": "USD",
                    "originalValue": 1.0,
                    "value": 1.00
        },
        "conversionRate": 0.75,
                "value": 0.75
    }*/

    private String total;
    private String id;
    private String subTotal;
    private String quantity;
    private String salePrice;
    private String salePriceLocked;
    private String totalPriceModifications;
    private String totalBeforeModifications;
    private String totalBeforeTaxes;
    private String totalEffectiveTaxRate;
    private String totalTaxableAmount;
    private String totalTaxes;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSalePriceLocked() {
        return salePriceLocked;
    }

    public void setSalePriceLocked(String salePriceLocked) {
        this.salePriceLocked = salePriceLocked;
    }

    public String getTotalPriceModifications() {
        return totalPriceModifications;
    }

    public void setTotalPriceModifications(String totalPriceModifications) {
        this.totalPriceModifications = totalPriceModifications;
    }

    public String getTotalBeforeModifications() {
        return totalBeforeModifications;
    }

    public void setTotalBeforeModifications(String totalBeforeModifications) {
        this.totalBeforeModifications = totalBeforeModifications;
    }

    public String getTotalBeforeTaxes() {
        return totalBeforeTaxes;
    }

    public void setTotalBeforeTaxes(String totalBeforeTaxes) {
        this.totalBeforeTaxes = totalBeforeTaxes;
    }

    public String getTotalEffectiveTaxRate() {
        return totalEffectiveTaxRate;
    }

    public void setTotalEffectiveTaxRate(String totalEffectiveTaxRate) {
        this.totalEffectiveTaxRate = totalEffectiveTaxRate;
    }

    public String getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setTotalTaxableAmount(String totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public String getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(String totalTaxes) {
        this.totalTaxes = totalTaxes;
    }
}
