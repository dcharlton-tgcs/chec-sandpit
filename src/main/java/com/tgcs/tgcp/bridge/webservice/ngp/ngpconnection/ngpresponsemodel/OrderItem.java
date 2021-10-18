package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.OrderItemType;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.Tare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderItem {

    /**
     * array[ReasonCode] (optional)
     */
    private List reasonCodes = new ArrayList();

    /**
     * array[Validation] (optional)
     */
    private List validations = new ArrayList();

    /**
     * (optional)
     */
    private String id;

    /**
     * OrderItemType (optional)
     */
    private OrderItemType type;

    /**
     * (optional)
     */
    private Map<String, Object> attributes;

    /**
     * (optional)
     */
    //   Action action;

    /**
     * The raw entry data used when looking up this item. (optional)
     */
    private String itemLabelData;

    /**
     * Used for bundle definitions to reference the bundle item. (optional)
     */
    private String parentOrderItemId;

    /**
     * array[ChildOrderItemRef]
     * This references the itemId/qty of child item(s) of an OrderItem. This field is used for item link chains, warranty associations, etc.
     * An item cannot be voided unless it's childItems is empty; this can be achieved by voiding the childItems with the parent.
     * (optional)
     */
    List<ChildOrderItemRef> childItems = new ArrayList<>();

    /**
     * Id number to reference against 'associates' at order level for looking up other fields.
     * (optional)
     */
    private String[] associateIds;

    /**
     * Models quantity or weight based on item's UOM.  (optional)
     */
    private BigDecimal quantity;

    /**
     * (optional)
     */
    private String serial;


    /**
     * (optional)
     */
    //   StoredValue storedValue


    /**
     * (optional)
     */
    private Boolean quantityEntered;

    /**
     * (optional)
     */
    @JsonProperty("tare")
    private Tare tare;

    /**
     * (optional)
     */
    private List<ItemPrice> prices = new ArrayList<>();

    /**
     *
     */
    private Item item;

    /**
     * The node (store/fulfillment-location/eCom/etc) that added this item.  (optional)
     */
    private String originNodeId;

    /**
     * ReferenceReturnOrderItem (optional)
     */
    //   ReferenceReturnOrderItem referenceReturnOrderItem;

    /**
     * The orderItem in this Order that this item is being exchanged against.
     * This is used for even exchange and must point to an OrderItem of type RETURN and is used to lookup pricing/etc to match for exchange.
     * (optional)
     */
    private String exchangeOrderItemId;

    public List getReasonCodes() {
        return reasonCodes;
    }

    public void setReasonCodes(List reasonCodes) {
        this.reasonCodes = reasonCodes;
    }

    public List getValidations() {
        return validations;
    }

    public void setValidations(List validations) {
        this.validations = validations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderItemType getType() {
        return type;
    }

    public void setType(OrderItemType type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getItemLabelData() {
        return itemLabelData;
    }

    public void setItemLabelData(String itemLabelData) {
        this.itemLabelData = itemLabelData;
    }

    public String getParentOrderItemId() {
        return parentOrderItemId;
    }

    public void setParentOrderItemId(String parentOrderItemId) {
        this.parentOrderItemId = parentOrderItemId;
    }

    public String[] getAssociateIds() {
        return associateIds;
    }

    public void setAssociateIds(String[] associateIds) {
        this.associateIds = associateIds;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Boolean getQuantityEntered() {
        return quantityEntered;
    }

    public void setQuantityEntered(Boolean quantityEntered) {
        this.quantityEntered = quantityEntered;
    }

    public Tare getTare() {
        return tare;
    }

    public void setTare(Tare tare) {
        this.tare = tare;
    }

    public List<ItemPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<ItemPrice> prices) {
        this.prices = prices;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getOriginNodeId() {
        return originNodeId;
    }

    public void setOriginNodeId(String originNodeId) {
        this.originNodeId = originNodeId;
    }

    public String getExchangeOrderItemId() {
        return exchangeOrderItemId;
    }

    public void setExchangeOrderItemId(String exchangeOrderItemId) {
        this.exchangeOrderItemId = exchangeOrderItemId;
    }



    public List<ChildOrderItemRef> getChildItems() {
        return childItems;
    }

    public OrderItem setChildItems(List<ChildOrderItemRef> childItems) {
        this.childItems = childItems;
        return this;
    }

    public String getItemDescription() {
        String itemDescription = "";
        if (item.getDescription() != null && item.getDescription().values().size() > 0) {
            // if we have key = default
            itemDescription = item.getDescription().containsKey("default") ?
                    item.getDescription().get("default").toString() : item.getDescription().values().stream().findFirst().toString();
        } else if (item.getDisplayName() != null && item.getDisplayName().values().size() > 0) {
            itemDescription = item.getDescription().values().stream().findFirst().toString();
        }else{
            itemDescription = "Not on File";
        }
        return itemDescription;
    }

}
