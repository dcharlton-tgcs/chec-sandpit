package com.tgcs.tgcp.bridge.checoperations;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderCoupon;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.adapter.PaymentInfo;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.EReceiptLineTrack;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.VoidItemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class BridgeSession {

    private OrderAdapter orderAdapterPreviousState = null;
    private OrderAdapter orderAdapter = null;
    private EReceiptLineTrack receiptItemsCache = new EReceiptLineTrack();

    @Autowired
    ConfProperties properties;

    public String getNGPStatus() {
        if (orderAdapter == null) {
            return TransactionStatusType.NOT_IN_TRANSACTION.getValue();
        }
        return TransactionStatusType.TRANSACTION_START.getValue();
    }

    public EReceiptLineTrack getReceiptItemsCache() {
        return receiptItemsCache;
    }

    public OrderAdapter getOrderAdapter() {
        return orderAdapter;
    }

    public void setOrderAdapter(OrderAdapter orderAdapter) {
        this.orderAdapterPreviousState = this.orderAdapter;
        this.orderAdapter = orderAdapter;
    }

    public OrderAdapter getOrderAdapterPreviousState() {
        return orderAdapterPreviousState;
    }

    public void clearOrder() {
        orderAdapter = null;
        orderAdapterPreviousState = null;
        receiptItemsCache = new EReceiptLineTrack();
    }

    public boolean inTransaction() {
        return orderAdapter != null;
    }

    public OrderCoupon getAddedCoupon(OrderCoupon orderCoupon) {
        List<OrderCoupon> couponFound = orderAdapter.getCouponList().stream()
                .filter(x -> orderCoupon.getItemIdentifier().equalsIgnoreCase(x.getItemIdentifier()))
                .collect(Collectors.toList());

        if (couponFound.isEmpty())
            return null;

        return couponFound.get(couponFound.size() - 1);

    }

    /**
     * Gets an {@code OrderItem} from the bridge session.
     * <p>
     * This is commonly needed because after each AddItemResult, the session will pe populated with a whole transaction,
     * containing not only the item that was just added, but all the previous items also.
     * In order to send the AddItemResult to the POSBC requester, the item that was just added needs to be identified.
     *
     * @param itemIdentifier barcode used to identify the item
     * @param id in case of regular items this usually is the barcode, in case of weighted items this is an unique string
     * @return an item or {@code Optional.empty()} if not found.
     * While this returns an {@code Optional}, not finding the added item is an exceptional case that probably should be handled with an
     * {@code IllegalStateException}.
     */
    public Optional<OrderItem> getAddedItem(String itemIdentifier, String id) {
        List<OrderItem> itemsFound = orderAdapter.getItemList().stream()
                                                 .filter(e -> itemIdentifier.equalsIgnoreCase(e.getItemIdentifier())) // filter items that don't match the barcode
                                                 .filter(e -> e.getParentOrderItemId().isEmpty()) // filter child items
                                                 .collect(Collectors.toList());

        if (itemsFound.size() > 1) {
            return getItemNotInCache(itemIdentifier, id);
        } else if (itemsFound.size() == 1) {
            return Optional.of(itemsFound.get(0));
        }

        return Optional.empty();
    }

    /**
     * Gets an {@code OrderItem} from the bridge session, that has not yet been added to the item cache.
     * <p>
     * Apparently this is the case for weighted items.
     * @param itemIdentifier barcode used to identify the item
     * @param id in case of regular items this usually is the barcode, in case of weighted items this is an unique string
     * @return an item or {@code Optional.empty()} if not found
     */
    private Optional<OrderItem> getItemNotInCache(String itemIdentifier, String id) {
        List<OrderItem> weightedItemsFound = orderAdapter.getItemList().stream()
                                                         .filter(e -> itemIdentifier.equalsIgnoreCase(e.getItemIdentifier()))
                                                         .filter(e -> !receiptItemsCache.containsContent(id))
                                                         .collect(Collectors.toList());

        if (weightedItemsFound.size() > 0) {
            return Optional.of(weightedItemsFound.get(0));
        }
        return Optional.empty();
    }

    /**
     *  Finds a payment with a specified id in the payment info list of the current order.
     *
     * @param paymentId to look for in the order payments
     * @return the first payment with a matching id or {@code Optional.empty()}
     */
    public Optional<PaymentInfo> getAddedPaymentById(String paymentId) {
        List<PaymentInfo> info = getOrderAdapter().getPaymentInfoList().stream()
                                                    .filter(e -> paymentId.equals(e.getPaymentId()))
                                                    .collect(Collectors.toList());

        if (info.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(info.get(0));
    }

    /**
     * Method used to retrieve the OrderItem associated with an item id from the current BridgeSession.
     * Useful when trying to retrieve linked order items, for example.
     * @param itemId used to identify the OrderItem
     * @return OrderItem
     */
    public OrderItem getLinkedItem(String itemId, String parentIdentificationNumber) {
        OrderItem linkedItem = orderAdapter.getItemList().stream()
                //TODO a filter for LinkedItem NGP
                //.filter(item -> item.getId().equals(itemId))
                .filter(item -> !properties.getPosType().contains("tpnet") ||
                        item.getParentOrderItemId().equals(parentIdentificationNumber))
                .collect(Collectors.toList()).get(0);
        return linkedItem;
    }

    public OrderItem getItem(String barcode){
        if(orderAdapter == null) return null;
        return orderAdapter.getItemList().stream()
                .filter(e -> e.getItemIdentifier().equalsIgnoreCase(barcode))
                .findFirst()
                .orElse(null);
    }

}
