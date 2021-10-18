package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.*;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.helper.ReceiptHelper;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.*;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatus;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.totals.TotalsEvent;
import com.tgcs.tgcp.bridge.checoperations.model.totals.TransactionTotals;
import com.tgcs.tgcp.bridge.common.utils.StringFormatterCustom;

import java.math.BigDecimal;

public class Ngp2ChecMapper {

    public static TotalsEvent createTotalEventFromOrder(OrderAdapter orderAdapter, String requestId) {
        return new TotalsEvent()
                .setRequestId(requestId)
                .setTransactionTotals(new TransactionTotals()
                        .setTotal(orderAdapter.getTotal().toString())
                        .setSubtotal(orderAdapter.getSubtotal().toString())
                        .setTax(orderAdapter.getTotalTax().toString())
                        .setBalanceDue(orderAdapter.getBalanceDue())
                        .setChangeDue(orderAdapter.getChangeDue())
                        .setCouponTotal(orderAdapter.getCouponTotal().toString())
                        .setTotalItems(orderAdapter.getTotalItemCount().toString())
                        .setTotalCoupons(orderAdapter.getCouponList().size() + "")
                        .setTotalSavings(orderAdapter.getTotalPriceModifications().toString())
                        .setTenderApplied(orderAdapter.getTotalPayments().toString()));
    }

    protected static TransactionStatusEvent createTransactionStatusEvent(OrderAdapter orderAdapter, String requestId, String transactionStatus) {
        return new TransactionStatusEvent()
                .setRequestId(requestId)
                .setTransactionStatus(new TransactionStatus()
                        .setStatus(transactionStatus)
                        .setCategory("sales")
                        .setId(orderAdapter.getOrderId())
                        .setType("regularSale")
                        .setDate(orderAdapter.getLastModifiedDate())
                        .setTime(orderAdapter.getLastModifiedTime()));
    }

    public static POSReceiptEvent createPOSReceiptEventHeader(String requestId) {
        return new POSReceiptEvent()
                .setRequestId(requestId)
                .setSection(ReceiptSection.HEADER.getValue())
                .setGroup(requestId)
                .setFormattedReceiptLineList(new FormattedReceiptLineList()
                        .addHeaderLines());
    }

    public static POSReceiptEvent createPOSReceiptEventItem(BridgeSession session, String requestId, OrderItem itemAdded) {
        // if weighted item, I won't use item cache
        // if weighted item we need to print a new line anyway
        FormattedReceiptLineList formattedReceiptLineList = new FormattedReceiptLineList();
        String total = itemAdded.getExtendedPrice().multiply(itemAdded.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();

        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.ITEM, ReceiptHelper.createFormattedReceiptLineItemPrice(itemAdded));
        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.ITEM, ReceiptHelper.createFormattedReceiptLine(itemAdded.getDescription(), total));

        for (ChildItem childItem : itemAdded.getChildItemList()) {
            OrderItem linkedItem = session.getLinkedItem(childItem.getId(), itemAdded.getParentIdentificationNumber());
            total = linkedItem.getExtendedPrice().multiply(linkedItem.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
            formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.ITEM, ReceiptHelper.createFormattedReceiptLineItemPrice(linkedItem));
            formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.ITEM, ReceiptHelper.createFormattedReceiptLine(linkedItem.getDescription(), total));
        }

        for (Discount disc : itemAdded.getDiscounts()){
            formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.COUPON, ReceiptHelper.createFormattedReceiptLine(disc.getDiscountName(), disc.getDiscountValue()));
        }

        return new POSReceiptEvent().setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.ITEM.getValue())
                .setFormattedReceiptLineList(formattedReceiptLineList);
    }

    public static POSReceiptEvent createRemovePOSReceiptEventItemLine(String requestId) {
        return new POSReceiptEvent().setUpdateType(POSReceiptEvent.UpdateType.Remove.name())
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.ITEM.getValue());
    }


    public static POSReceiptEvent createPOSReceiptEventCoupon(String requestId, OrderCoupon orderCoupon) {
        return new POSReceiptEvent()
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.ITEM.getValue())
                .setFormattedReceiptLineList(new FormattedReceiptLineList()
                        .addReceiptLine(LineCategory.ITEM, LineType.COUPON, ReceiptHelper.createFormattedReceiptLine(orderCoupon.getDescription(), orderCoupon.getValue().toString())));
    }

    public static POSReceiptEvent createVoidPOSReceiptEvent(String requestId) {
        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.TRAILER.getValue())
                .setFormattedReceiptLineList(new FormattedReceiptLineList()
                        .addReceiptLine(LineCategory.ITEM, LineType.VOID_TRANSACTION, "*VOID TRANSACTION*")
                        .addReceiptLine(LineCategory.ITEM, LineType.VOID_TRANSACTION, ReceiptHelper.createFormattedReceiptLineDate()));
    }

    public static POSReceiptEvent createSuspendPOSReceiptEvent(String requestId) {
        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.TRAILER.getValue())
                .setFormattedReceiptLineList(new FormattedReceiptLineList()
                        .addReceiptLine(LineCategory.ITEM, LineType.SUSPEND_TRANSACTION, "*TRANSACTION SUSPENDED*")
                        .addReceiptLine(LineCategory.ITEM, LineType.SUSPEND_TRANSACTION, ReceiptHelper.createFormattedReceiptLineDate()));
    }

    public static POSReceiptEvent createAddTenderPOSReceiptEvent(BridgeSession session, PaymentInfo paymentInfo) {
        FormattedReceiptLineList formattedReceiptLineList = new FormattedReceiptLineList();
        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER,
                ReceiptHelper.createFormattedReceiptLine("        Cash", paymentInfo.getAmount()));

        if (session.getOrderAdapter().getPaymentShortage().doubleValue() < 0) {
            formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.CHANGE,
                    ReceiptHelper.createFormattedReceiptLine("        Change", session.getOrderAdapter().getChangeDue()));
        }

        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(paymentInfo.getRequestId())
                .setGroup(paymentInfo.getRequestId())
                .setSection(ReceiptSection.BODY.getValue())
                .setFormattedReceiptLineList(formattedReceiptLineList);
    }

    public static POSReceiptEvent createDebitTenderPOSReceiptEvent(PaymentInfo paymentInfo) {
        FormattedReceiptLineList formattedReceiptLineList = new FormattedReceiptLineList();
        formattedReceiptLineList.addReceiptLine(LineCategory.TRAILER, LineType.WORKSTATION_INFO,
                ReceiptHelper.createFormattedReceiptLineDate());

        formattedReceiptLineList.addReceiptLine(LineCategory.TRAILER, LineType.LOYALTY_MESSAGES,
                StringFormatterCustom.createPaddingString('*', 37));

        formattedReceiptLineList.addReceiptLine(LineCategory.TRAILER, LineType.LOYALTY_MESSAGES,
                "Thank you for shopping with us.");

        formattedReceiptLineList.addReceiptLine(LineCategory.TRAILER, LineType.LOYALTY_MESSAGES,
                StringFormatterCustom.createPaddingString('*', 37));


        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(paymentInfo.getRequestId())
                .setGroup(paymentInfo.getRequestId())
                .setSection(ReceiptSection.BODY.getValue())
                .setFormattedReceiptLineList(formattedReceiptLineList);
    }

    public static POSReceiptEvent createDebitTenderingPOSReceiptEvent(PaymentInfo paymentInfo) {
        FormattedReceiptLineList formattedReceiptLineList = new FormattedReceiptLineList();
        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER_FRANKING,
                StringFormatterCustom.createPaddingString('-', 37));

        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER_FRANKING,
                ReceiptHelper.createFormattedReceiptLineDate());

        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER_FRANKING,
                ReceiptHelper.createFormattedReceiptLine("Amount " + paymentInfo.getAmount(), ""));

        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER_FRANKING,
                paymentInfo.getCardDetails().getMaskedAccountNumber());

        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER_FRANKING, "APPROVED");

        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER_FRANKING,
                StringFormatterCustom.createPaddingString('-', 37));

        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(paymentInfo.getRequestId())
                .setGroup(paymentInfo.getRequestId())
                .setSection(ReceiptSection.BODY.getValue())
                .setFormattedReceiptLineList(formattedReceiptLineList);
    }

    public static POSReceiptEvent createMiscTenderPOSReceiptEvent(PaymentInfo paymentInfo) {
        FormattedReceiptLineList formattedReceiptLineList = new FormattedReceiptLineList();
        formattedReceiptLineList.addReceiptLine(LineCategory.ITEM, LineType.TENDER,
                ReceiptHelper.createFormattedReceiptLine(paymentInfo.getPaymentType(), paymentInfo.getAmount()));

        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(paymentInfo.getRequestId())
                .setGroup(paymentInfo.getRequestId())
                .setSection(ReceiptSection.BODY.getValue())
                .setFormattedReceiptLineList(formattedReceiptLineList);
    }

    public static POSReceiptEvent createPOSReceiptEventCustomer(String requestId, OrderCustomer orderCustomer) {
        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.BODY.getValue())
                .setFormattedReceiptLineList(new FormattedReceiptLineList()
                        .addReceiptLine(LineCategory.ITEM, LineType.CUSTOMER_ID_ENTRY,
                                orderCustomer.getCustomerName().getFirstName()
                                        + " " + orderCustomer.getCustomerName().getLastName()
                                        + " " + orderCustomer.getLoyaltyCard()));
    }

    public static POSReceiptEvent createPOSReceiptEventAddReceiptLines(String requestId, FormattedReceiptLine formattedReceiptLine) {
        return new POSReceiptEvent()
                .setUpdateType(POSReceiptEvent.UpdateType.Add.name())
                .setRequestId(requestId)
                .setGroup(requestId)
                .setSection(ReceiptSection.BODY.getValue())
                .setFormattedReceiptLineList(new FormattedReceiptLineList()
                        .addReceiptLine(formattedReceiptLine));
    }
}
