package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

public class ItemList {
    private String id;
    private String type;
    private Item item;
    private Integer quantity;
    private Prices[] prices;
    private String originNodeId;
    private String itemLabelData;
    private Boolean quantityEntered;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Prices[] getPrices() {
        return prices;
    }

    public void setPrices(Prices[] prices) {
        this.prices = prices;
    }

    public String getOriginNodeId() {
        return originNodeId;
    }

    public void setOriginNodeId(String originNodeId) {
        this.originNodeId = originNodeId;
    }

    public String getItemLabelData() {
        return itemLabelData;
    }

    public void setItemLabelData(String itemLabelData) {
        this.itemLabelData = itemLabelData;
    }

    public Boolean getQuantityEntered() {
        return quantityEntered;
    }

    public void setQuantityEntered(Boolean quantityEntered) {
        this.quantityEntered = quantityEntered;
    }
}
