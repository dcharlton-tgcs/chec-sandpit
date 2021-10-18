package com.tgcs.tgcp.bridge.checoperations.helper;

import com.tgcs.tgcp.bridge.adapter.Discount;
import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderHelper {

    public static List<OrderItem> compareStates(OrderAdapter order1, OrderAdapter order2) {
        List<OrderItem> modifiedItems = new ArrayList<>();
        if(order1 == null) return modifiedItems;

        List<OrderItem> items1 = order1.getItemList();
        List<OrderItem> items2 = order2.getItemList();
        for (OrderItem item2 : items2) {
            if(!item2.getParentOrderItemId().trim().isEmpty()) continue; //child item

            List<OrderItem> sameItems = items1.stream()
                    .filter(e -> e.getItemIdentifier().equalsIgnoreCase(item2.getItemIdentifier()))
                    .collect(Collectors.toList());
            if (sameItems.size() == 0) continue; // new item, not a modified item
            if (sameItems.size() == 1) {
                if (!hasSameDiscounts(sameItems.get(0), item2)) {
                    modifiedItems.add(item2);
                }
                continue;
            }

            //if sameItems.size() > 1
            boolean discFound = false;
            for (OrderItem itm : sameItems) {
                if (hasSameDiscounts(itm, item2)) {
                    discFound = true;
                    break;
                }
            }

            if (!discFound) modifiedItems.add(item2);

        }
        return modifiedItems;
    }

    public static boolean hasSameDiscounts(OrderItem item1, OrderItem item2) {
        if (item1.getDiscounts().size() != item2.getDiscounts().size()) {
            return false;
        }

        if(item1.getDiscounts().size() == 0 || item2.getDiscounts().size() == 0) return true;

        for (Discount disc : item1.getDiscounts()) {
            for (Discount disc2 : item2.getDiscounts()) {
                if (disc.equals(disc2)) {
                    return true;
                }
            }
        }
        return false;
    }

}
