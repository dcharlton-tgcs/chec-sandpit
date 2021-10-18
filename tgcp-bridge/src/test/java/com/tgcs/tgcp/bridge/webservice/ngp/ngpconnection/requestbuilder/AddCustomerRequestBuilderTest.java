package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.adapter.Item;
import com.tgcs.tgcp.bridge.adapter.OrderCustomer;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.AddCustomerRequest;
import com.tgcs.tgcp.pos.model.BarcodeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfProperties.class})
public class AddCustomerRequestBuilderTest {

    @Autowired
    ConfProperties confProperties;

    @Test
    public void testNullRequest() {
        AddCustomerRequest addCustomerRequest = AddCustomerRequestBuilder.build(new Item(), confProperties);
        assertNull(addCustomerRequest);
    }

    @Test
    public void testBuildRequest() {
        AddCustomerRequest addCustomerRequest = AddCustomerRequestBuilder.build(
                new OrderCustomer()
                        .setCustomerId("2622166053300"),
                confProperties);
        assertNotNull(addCustomerRequest);
        assertNotNull(addCustomerRequest.getContext());
        assertEquals("2622166053300", addCustomerRequest.getCustomerId());
    }
}
