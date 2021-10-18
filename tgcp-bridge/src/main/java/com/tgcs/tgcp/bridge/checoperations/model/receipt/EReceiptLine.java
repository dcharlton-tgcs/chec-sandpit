package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.adapter.Item;
import com.tgcs.tgcp.bridge.adapter.OrderItem;

import java.util.Objects;

public class EReceiptLine {

    /**
     * This is the id to identify the line in the receipt
     * for chec here should be the requestId
     */
    private String lineId;

    /**
     * Here we store the item number displayed at the line with id @lineId
     */
    private String lineContent;

    /**
     * The full item information
     */
    private Item item;

    public EReceiptLine(String lineContent, String lineId, Item item) {
        this.lineId = lineId;
        this.lineContent = lineContent;
        this.item = item;
    }

    public String getLineId() {
        return lineId;
    }


    public String getLineContent() {
        return lineContent;
    }

    public EReceiptLine setLineId(String lineId) {
        this.lineId = lineId;
        return this;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EReceiptLine)) return false;
        EReceiptLine that = (EReceiptLine) o;
        return lineId.equals(that.lineId) &&
                lineContent.equals(that.lineContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, lineContent);
    }
}
