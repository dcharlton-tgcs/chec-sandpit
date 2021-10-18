package com.tgcs.tgcp.bridge.checoperations.helper;

import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.common.utils.StringFormatterCustom;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptHelper {
    private static int CHEC_RECEIPT_LINE_LENGTH = 40;

    public static String createFormattedReceiptLine(String startingLine, String endingLine) {
        String paddingString = StringFormatterCustom.createPaddingString(' ', CHEC_RECEIPT_LINE_LENGTH - startingLine.length() - endingLine.length());
        return startingLine + paddingString + endingLine;
    }

    public static String createFormattedReceiptLineItemPrice(OrderItem orderItem) {
        int paddingBeforeLength = 15;

        String quantity = (orderItem.getQuantity().setScale(orderItem.isWeightedItem() ? 2 : 0, BigDecimal.ROUND_HALF_EVEN)).toString();
        String lineContent = quantity + (orderItem.isWeightedItem()  ?  orderItem.getUom() : "" ) + " x " + orderItem.getExtendedPrice().toString();

        String paddingBefore = StringFormatterCustom.createPaddingString(' ', paddingBeforeLength);
        String paddingAfter = StringFormatterCustom.createPaddingString(' ', CHEC_RECEIPT_LINE_LENGTH - paddingBeforeLength - lineContent.length());
        return paddingBefore + lineContent + paddingAfter;
    }

    public static String createFormattedReceiptLineDate() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yy hh:mm a"));
        String padding = StringFormatterCustom.createPaddingString(' ', 37 - date.length());
        return date + padding;
    }

}
