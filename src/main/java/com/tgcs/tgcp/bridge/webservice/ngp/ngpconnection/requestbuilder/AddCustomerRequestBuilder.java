package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.adapter.ChecAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderCustomer;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.AddCustomerRequest;

public class AddCustomerRequestBuilder {

    public static AddCustomerRequest build(ChecAdapter orderCustomer, ConfProperties properties) {
        if (!(orderCustomer instanceof OrderCustomer)) return null;
        OrderCustomer customer = (OrderCustomer) orderCustomer;
        return new AddCustomerRequest()
                .customerId(customer.getCustomerId())
                .context(ContextBuilder.build(properties));
    }
}
