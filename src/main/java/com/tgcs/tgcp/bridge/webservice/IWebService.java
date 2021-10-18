package com.tgcs.tgcp.bridge.webservice;

import com.tgcs.tgcp.bridge.adapter.*;

public interface IWebService {

    void initializeWebService() throws Exception;

    OrderAdapter addItem(OrderItem orderItem) throws Exception;

    OrderAdapter addCoupon(OrderCoupon orderCoupon) throws Exception;

    OrderAdapter addCustomer(OrderCustomer orderCustomer) throws Exception;

    OrderAdapter addPayment(PaymentInfo paymentDetails) throws Exception;

    OrderAdapter voidItem(OrderItem orderItem) throws Exception;

    boolean finishTransaction();

    void signOff();

    void suspendTransaction() throws Exception;

    void voidTransaction() throws Exception;

    void releaseResources();

    void printReceipt() throws Exception;

}
