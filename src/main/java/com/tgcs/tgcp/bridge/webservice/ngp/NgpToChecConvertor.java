package com.tgcs.tgcp.bridge.webservice.ngp;

import com.tgcs.tgcp.bridge.adapter.CustomerName;
import com.tgcs.tgcp.bridge.adapter.OrderCustomer;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.adapter.*;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.OrderItemType;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel.LoyaltyType;
import com.tgcs.tgcp.pos.model.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NgpToChecConvertor {

    public static OrderAdapter convertNgpOrderToChecOrder(Order ngpOrderResponse) {
        return new OrderAdapter()
                .setOrderId(ngpOrderResponse.getId())
                .setOrderNumber(ngpOrderResponse.getOrderNumber())
                .setTotal(ngpOrderResponse.getTotal())
                .setSubtotal(ngpOrderResponse.getTotalBeforeTaxes())
                .setTotalPayments(ngpOrderResponse.getTotalPayments())
                .setTotalTax(ngpOrderResponse.getTotalTaxes())
                .setTotalItemCount(ngpOrderResponse.getTotalItemCount())
                .setPaymentShortage(ngpOrderResponse.getPaymentShortage())
                .setTotalPriceModifications(ngpOrderResponse.getTotalPriceModifications())
                .setCreateTimestamp(ngpOrderResponse.getCreateTimestamp().toString())
                .setLastModifiedTimestamp(ngpOrderResponse.getLastModifiedTimestamp().toString())
                .setItemList(Optional.ofNullable(ngpOrderResponse.getItems()).orElseGet(Collections::emptyList)
                        .stream()
                        .map(NgpToChecConvertor::convertNgpOrderItemToChecOrderItem)
                        .collect(Collectors.toList()))
                .setPaymentInfoList(getPaymentInfoList(ngpOrderResponse))
                .setCouponList(Optional.ofNullable(ngpOrderResponse.getPriceModifications()).orElseGet(Collections::emptyList)
                        .stream()
                        .map(NgpToChecConvertor::convertNgpPriceModificationToChecCoupon)
                        .collect(Collectors.toList()))
                // todo: I think we will need to refactor this once we refactor coupon handling. At the moment we won't display this value on CHEC
                .setCouponTotal(Optional.ofNullable(ngpOrderResponse.getPriceModifications()).orElseGet(Collections::emptyList)
                        .stream()
                        .filter(e -> e.getCoupon())
                        .map(e->e.getValue())
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .setOrderCustomer(ngpOrderResponse.getCustomer() != null ? convertNgpCustomerToChecCustomer(ngpOrderResponse) : null);
    }

    private static OrderCustomer convertNgpCustomerToChecCustomer(Order response) {
        return new OrderCustomer()
                .setCustomerName(new CustomerName()
                        .setFirstName(response.getCustomer().getName().getFirstName())
                        .setLastName(response.getCustomer().getName().getLastName()))
                .setCustomerId(response.getCustomer().getId())
                .setLoyaltyCard(response.getCustomer().getLoyalties().stream()
                        .filter(x -> x.getLoyaltyType().equals(LoyaltyType.LOYALTY_CARD.name())).findFirst().orElse(new CustomerLoyalty().loyaltyId("")).getLoyaltyId());
    }

    private static OrderCoupon convertNgpPriceModificationToChecCoupon(com.tgcs.tgcp.pos.model.PriceModification ngpPriceModification) {
        return (OrderCoupon) new OrderCoupon()
                .setValue(ngpPriceModification.getAdjustmentAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).negate())
                .setId(ngpPriceModification.getId())
                .setReturnFlag(ngpPriceModification.getType().equals(OrderItemType.RETURN))
                .setItemIdentifier(ngpPriceModification.getOfferId())
                .setDescription(ngpPriceModification.getCustomerMessage().get("default"))
                .setQuantity(BigDecimal.valueOf(1));
    }


    public static OrderItem convertNgpOrderItemToChecOrderItem(com.tgcs.tgcp.pos.model.OrderItem ngpOrderItem) {
        OrderItem orderItem = (OrderItem) new OrderItem()
                .setParentOrderItemId(ngpOrderItem.getParentOrderItemId())
                .setParentIdentificationNumber(Optional.ofNullable(ngpOrderItem.getAdditional().get("LINE_CREATE_NUMBER")).orElse("").toString())
                .setChildItemList(Optional.ofNullable(ngpOrderItem.getChildItems()).orElseGet(Collections::emptyList)
                        .stream()
                        .map(NgpToChecConvertor::convertNgpItemChildToChec)
                        .collect(Collectors.toList()))
                .setNotForSale(Optional.ofNullable(ngpOrderItem.getItem().getNotForSale()).orElse(Boolean.FALSE))
                .setNotOnFileItem(Optional.ofNullable(ngpOrderItem.getItem().getIsNotOnFileItem()).orElse(Boolean.FALSE))
                // Setting flag if item is a weighted one
                .setWeightedItem(!ngpOrderItem.getItem().getIsUOMTypeEach())
                // Setting UOM in the appropriate field, will be used for the receipt lines
                .setUom(ngpOrderItem.getItem().getUOM())
                .setId(ngpOrderItem.getId())
                .setReturnFlag(ngpOrderItem.getType().equals(OrderItemType.RETURN))
                .setItemIdentifier(ngpOrderItem.getItemLabelData())
                .setDescription(ngpOrderItem.getItem().getDescription().get("default"))
                .setQuantity(ngpOrderItem.getQuantity())
                .setRestrictedAge(getRestrictedAge(ngpOrderItem.getItem()))
                .setExtendedPrice(ngpOrderItem.getPrices().get(0).getSalePrice().setScale(2, BigDecimal.ROUND_HALF_EVEN));

        //convert price modification and add them to the orderItem as discounts
        ngpOrderItem.getPrices().stream()
                .map(ItemPrice::getPriceModifications)
                .filter(e -> e!=null)
                .flatMap(Collection::stream)
                .forEach (e-> addDiscountToItem(orderItem, e));

        return orderItem;
    }

    protected static void addDiscountToItem(OrderItem orderItem, PriceModification priceModification){
        String discName = null;
        if(priceModification.getCustomerMessage() != null && priceModification.getCustomerMessage().get("default") != null){
            discName = priceModification.getCustomerMessage().get("default");
        }
        if(discName != null || priceModification.getValue() != null){
            orderItem.getDiscounts().add(new Discount(discName, priceModification.getValue()));
        }
    }

    private static int getRestrictedAge(com.tgcs.tgcp.pos.model.Item ngpItem) {
        int restrictedAge = 0;
        if (ngpItem.getAttributes() != null && ngpItem.getAttributes().get("MIN_AGE_TO_SELL") != null) {
            restrictedAge = (int) ngpItem.getAttributes().get("MIN_AGE_TO_SELL");
        }
        return restrictedAge;
    }

    private static ChildItem convertNgpItemChildToChec(com.tgcs.tgcp.pos.model.ChildOrderItemRef childOrderItemRef) {
        return new ChildItem()
                .setId(childOrderItemRef.getId())
                .setQuantity(childOrderItemRef.getQuantity());
    }

    public static PaymentInfo convertNgpPaymentToChecTender(com.tgcs.tgcp.pos.model.Payment ngpPayment) {
        return new PaymentInfo()
                .setPaymentId(ngpPayment.getId())
                .setSignatureNeeded(Optional.ofNullable(ngpPayment.getSignatureRequired()).orElse(false))
                .setVoid(Optional.ofNullable(ngpPayment.getVoided()).orElse(false));
    }

    public static PaymentInfo convert(DepositRefundVoucherPayment depositRefundVoucherPayment) {
        DepositRefundVoucher depositRefundVoucher = depositRefundVoucherPayment.getDepositRefundVoucher();
        return new PaymentInfo()
                .setPaymentId(depositRefundVoucher.getUniqueId())
                .setAmount(depositRefundVoucher.getTotalVoucherAmount().toString())
                .setCurrency(depositRefundVoucher.getCurrencyCode().value);
    }

    /**
     * Returns a {@code List<PaymentInfo>} from all the payments found in the {@code Order}, including actual payments and deposit refund vouchers.
     *
     * @param order from which payments are extracted
     * @return list containing all order payment information
     */
    private static List<PaymentInfo> getPaymentInfoList(Order order) {
        List<Payment> payments = Optional.ofNullable(order.getPayments()).orElseGet(Collections::emptyList);
        List<PaymentInfo> paymentInfoList = payments.stream().map(NgpToChecConvertor::convertNgpPaymentToChecTender).collect(Collectors.toList());

        List<DepositRefundVoucherPayment> drvPayments = Optional.ofNullable(order.getDepositRefundVoucherPaymentInfo()).orElseGet(Collections::emptyList);
        paymentInfoList.addAll(drvPayments.stream().map(NgpToChecConvertor::convert).collect(Collectors.toList()));
        return paymentInfoList;
    }
}
