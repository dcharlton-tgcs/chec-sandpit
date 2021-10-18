package com.tgcs.tgcp.bridge.checoperations.helper;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.util.TestSupport;
import com.tgcs.tgcp.bridge.webservice.ngp.NgpToChecConvertor;
import com.tgcs.tgcp.pos.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {OrderHelper.class})
public class OrderHelperTest {

    Order previousNGPOrder = new Order();

    Order currentNGPOrder = new Order();

    OrderAdapter previousOrderAdapter = new OrderAdapter();
    OrderAdapter currentOrderAdapter = new OrderAdapter();


    @Test
    public void testCompareOrdersNoModifiedItems() {

        previousNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_no_discounts.json");
        currentNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_2items_no_discounts.json");

        previousOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        currentOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);


        List<OrderItem> modifiedItems = OrderHelper.compareStates(previousOrderAdapter, currentOrderAdapter);

        assertEquals(0, modifiedItems.size());

    }

    @Test
    public void testCompareOrders2ModifiedItems() {

        previousNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_2items_no_discounts.json");
        currentNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_3items_2_with_discounts.json");

        previousOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        currentOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);


        List<OrderItem> modifiedItems = OrderHelper.compareStates(previousOrderAdapter, currentOrderAdapter);

        assertEquals(2, modifiedItems.size());
        assertEquals("Lunchtime", modifiedItems.get(0).getDiscounts().get(0).getDiscountName());
        assertEquals("-1.49", modifiedItems.get(0).getDiscounts().get(0).getDiscountValue());

        assertEquals("Lunchtime", modifiedItems.get(1).getDiscounts().get(0).getDiscountName());
        assertEquals("-0.01", modifiedItems.get(1).getDiscounts().get(0).getDiscountValue());

    }

    @Test
    public void testCompareOrdersSameDiscount() {

        previousNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_3items_2_with_discounts.json");
        currentNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_3items_2_with_discounts.json");

        previousOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        currentOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);


        List<OrderItem> modifiedItems = OrderHelper.compareStates(previousOrderAdapter, currentOrderAdapter);

        assertEquals(0, modifiedItems.size());

    }

    @Test
    public void testCompareOrdersSameDiscountNumberDifferentValue() {

        previousNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_3items_2_with_discounts.json");
        currentNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_1item_same_discount.json");

        previousOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        currentOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);


        List<OrderItem> modifiedItems = OrderHelper.compareStates(previousOrderAdapter, currentOrderAdapter);

        assertEquals(1, modifiedItems.size());
        assertEquals("Lunchtime", modifiedItems.get(0).getDiscounts().get(0).getDiscountName());
        assertEquals("-1.48", modifiedItems.get(0).getDiscounts().get(0).getDiscountValue());

    }

    @Test
    public void testCompareOrdersSameItemTwice(){

        previousNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_sameItemTwice.json");
        currentNGPOrder = TestSupport.getObject(Order.class, "orderHelper/order_sameItemTwice_2_discounts.json");

        previousOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        currentOrderAdapter = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);


        List<OrderItem> modifiedItems = OrderHelper.compareStates(previousOrderAdapter, currentOrderAdapter);

        assertEquals(1, modifiedItems.size());
        assertEquals(2, modifiedItems.get(0).getDiscounts().size());

        assertEquals("Lunchtime", modifiedItems.get(0).getDiscounts().get(0).getDiscountName());
        assertEquals("-1.49", modifiedItems.get(0).getDiscounts().get(0).getDiscountValue());

        assertEquals("Lunchtime++", modifiedItems.get(0).getDiscounts().get(1).getDiscountName());
        assertEquals("-0.51", modifiedItems.get(0).getDiscounts().get(1).getDiscountValue());

    }





}
