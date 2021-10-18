package com.tgcs.tgcp.bridge.webservice.ngp;

import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.pos.model.Item;
import com.tgcs.tgcp.pos.model.ItemPrice;
import com.tgcs.tgcp.pos.model.OrderItemType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NgpToChecConvertorTest {

    @Test
    public void testAgeRestrictedItemConversion() throws Exception {
        Item item = new Item();
        item.putAttributesItem("MIN_AGE_TO_SELL", 18);
        item.setIsUOMTypeEach(true);
        item.putDescriptionItem("default", "test");
        com.tgcs.tgcp.pos.model.OrderItem ngpOrderItem = new com.tgcs.tgcp.pos.model.OrderItem().item(item);
        ngpOrderItem.setType(OrderItemType.SALE);
        ngpOrderItem.addPricesItem(new ItemPrice().salePrice(BigDecimal.ZERO));

        OrderItem bridgeOrderItem = NgpToChecConvertor.convertNgpOrderItemToChecOrderItem(ngpOrderItem);

        int restrictedAge = bridgeOrderItem.getRestrictedAge();
        assertEquals(18, restrictedAge);
    }

    @Test
    public void testExtendedPriceDecimalDigits() {
        Item item = new Item();
        com.tgcs.tgcp.pos.model.OrderItem orderItem = new com.tgcs.tgcp.pos.model.OrderItem();
        item.setIsUOMTypeEach(true);
        item.putDescriptionItem("default", "test");
        BigDecimal okSalePrice = new BigDecimal("2.29");

        com.tgcs.tgcp.pos.model.OrderItem ngpOrderItem = new com.tgcs.tgcp.pos.model.OrderItem().item(item);
        ngpOrderItem.setType(OrderItemType.SALE);
        ngpOrderItem.addPricesItem(new ItemPrice().salePrice(okSalePrice));

        OrderItem bridgeOrderItem = NgpToChecConvertor.convertNgpOrderItemToChecOrderItem(ngpOrderItem);

        assertEquals(okSalePrice, bridgeOrderItem.getExtendedPrice());
    }

    @Test
    public void testExtendedPriceDecimalDigitsRoundDown() throws Exception {
        Item item = new Item();
        com.tgcs.tgcp.pos.model.OrderItem orderItem = new com.tgcs.tgcp.pos.model.OrderItem();
        item.setIsUOMTypeEach(true);
        item.putDescriptionItem("default", "test");
        BigDecimal errorSalePrice = new BigDecimal("2.293");
        BigDecimal checkSalePrice = new BigDecimal("2.29");

        com.tgcs.tgcp.pos.model.OrderItem ngpOrderItem = new com.tgcs.tgcp.pos.model.OrderItem().item(item);
        ngpOrderItem.setType(OrderItemType.SALE);
        ngpOrderItem.addPricesItem(new ItemPrice().salePrice(errorSalePrice));

        OrderItem bridgeOrderItem = NgpToChecConvertor.convertNgpOrderItemToChecOrderItem(ngpOrderItem);

        assertEquals(checkSalePrice, bridgeOrderItem.getExtendedPrice());
    }

    @Test
    public void testExtendedPriceDecimalDigitsRoundUp() throws Exception {
        Item item = new Item();
        com.tgcs.tgcp.pos.model.OrderItem orderItem = new com.tgcs.tgcp.pos.model.OrderItem();
        item.setIsUOMTypeEach(true);
        item.putDescriptionItem("default", "test");
        BigDecimal errorSalePrice = new BigDecimal("2.297");
        BigDecimal checkSalePrice = new BigDecimal("2.30");

        com.tgcs.tgcp.pos.model.OrderItem ngpOrderItem = new com.tgcs.tgcp.pos.model.OrderItem().item(item);
        ngpOrderItem.setType(OrderItemType.SALE);
        ngpOrderItem.addPricesItem(new ItemPrice().salePrice(errorSalePrice));

        OrderItem bridgeOrderItem = NgpToChecConvertor.convertNgpOrderItemToChecOrderItem(ngpOrderItem);

        assertEquals(checkSalePrice, bridgeOrderItem.getExtendedPrice());
    }
}
