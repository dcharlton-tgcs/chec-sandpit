package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.totals.GetTotalsResponse;
import com.tgcs.tgcp.bridge.checoperations.model.totals.GetTotalsResult;
import com.tgcs.tgcp.bridge.checoperations.model.totals.TransactionTotals;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetTotalsHandler implements IHandler, ChecMessagePath {

    private static Logger logger = LoggerFactory.getLogger(GetTotalsHandler.class);
    private BridgeSession bridgeSession;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(GET_TOTALS_REQUEST_ID, "");

        //Send get totals response
        client.sendResponseToChecApp(createGetTotalsResponse(bridgeSession.getOrderAdapter(), requestId));
    }

    public GetTotalsResponse createGetTotalsResponse(OrderAdapter orderAdapter, String requestId) {
        return new GetTotalsResponse()
                .setGetTotalsResult(new GetTotalsResult()
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
                                .setTenderApplied(orderAdapter.getTotalPayments().toString())));
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }
}
