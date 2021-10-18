package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import java.math.BigDecimal;

public class ItemPrice {

/*

    reasonCodes (optional)
    array[ReasonCode]

    validations (optional)
    array[Validation]
  */

    /**
     * (optional)
     */
    private String id;

    /**
     * (optional)
     */
    private BigDecimal quantity;

    /**
     * The quantity of this ItemPrice that has been returned. Cannot exceed quantity. (optional)
     */
    private BigDecimal quantityReturned;

    /**
     * The salePrice (quantity N tiered price for the item) for a single item. (optional)
     */
    private BigDecimal salePrice;

  /*
            salePriceLocked (optional)
    Boolean Set by the system when price is manually set by the user for an item quantity.


            listPrice (optional)
    ConvertedCurrencyValue


    priceModifications (optional)
    array[PriceModification] List of all price modifications (surcharge,discount,fee,etc) applied to this item.

            combinationIds (optional)
    array[String] List of all the combinations that this item price contributes too without receiving a price modification


    taxExemptFlag (optional)
    Boolean The flag indicates if the buyer/customer on the order is exempted from paying taxes on this order item


    taxes (optional)
    array[Tax] List of all aggregate taxes applied to this item usually aggregated by tax type either by the provider or the tax service.


    taxDetails (optional)
    array[TaxDetail] List of all individual taxes with details applied to this item usually obtained from the external tax provider calculations.


            total (optional)
    BigDecimal The grand total of this item including all quantities, modifications, prices, taxes etc.


            totalBeforeModifications (optional)
    BigDecimal The total of all quantities of this item before modifications, taxes, etc.

            totalPriceModifications (optional)
    BigDecimal Grand total of all price modifications on this item.

            totalBeforeTaxes (optional)
    BigDecimal The total of this item including all quantities, modifications, prices but before taxes.

            subTotal (optional)
    BigDecimal The total of this item including all quantities, modifications, prices (with or without tax depending on wether the itemprices are tax included or not - EU/US). It is up to the tax service to do the right calculation.

            totalTaxableAmount (optional)
    BigDecimal Amount to use for the tax calculation (items and discounts with tax exemption are not included in this amount). This is calculated by the totals processor before invoking the tax service.

    totalTaxes (optional)
    BigDecimal The total of all taxes on this item. This is usually informational and computed by the tax provider.

    totalEffectiveTaxRate (optional)
    BigDecimal Effective tax rate applied on this item. This is usually informational and computed by the tax provider.

    excludedBundleItems (optional)
    array[String] This list contains skuIds that are missing from the return order to complete the bundle
*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityReturned() {
        return quantityReturned;
    }

    public void setQuantityReturned(BigDecimal quantityReturned) {
        this.quantityReturned = quantityReturned;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}
