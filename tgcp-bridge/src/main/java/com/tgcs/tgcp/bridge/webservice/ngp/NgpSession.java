package com.tgcs.tgcp.bridge.webservice.ngp;

import com.tgcs.posbc.bridge.printer.model.Receipt;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel.OrderResponse;
import com.tgcs.tgcp.pos.model.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NgpSession {

    private String authorizationKey = null;
    //FIXME delete this after refactoring complete
    private OrderResponse orderResponse = null;
    private Order ngpOrderResponse = null;
    private String currencyCode;
    private Receipt receipt;


    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public NgpSession setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
        return this;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public NgpSession setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
        return this;
    }

    public Order getNgpOrderResponse() {
        return ngpOrderResponse;
    }

    public NgpSession setNgpOrderResponse(Order orderResponse) {
        this.ngpOrderResponse = orderResponse;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public NgpSession setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public NgpSession setReceipt(Receipt receipt) {
        this.receipt = receipt;
        return this;
    }
}
