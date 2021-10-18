package com.tgcs.tgcp.bridge.print;

import com.tgcs.posbc.bridge.printer.model.Receipt;
import com.tgcs.posbc.bridge.printer.model.ReceiptData;
import com.tgcs.tgcp.pos.model.Order;

public class ReceiptBuilder {

    public static Receipt buildReceipt(Order order){
        if(order != null && order.getAdditional() != null && order.getAdditional().get("TPRS_RECEIPT") != null){
            return TprsReceiptBuilder.buildReceipt((String) order.getAdditional().get("TPRS_RECEIPT"));

        }
        return null;

    }

}
