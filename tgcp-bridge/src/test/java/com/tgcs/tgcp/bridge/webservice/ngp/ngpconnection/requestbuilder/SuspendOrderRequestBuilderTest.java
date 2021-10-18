package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.SubmitOrderRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfProperties.class})
public class SuspendOrderRequestBuilderTest {

    @Autowired
    ConfProperties confProperties;

    @Test
    public void testContextNotNull() {
        SubmitOrderRequest submitOrderRequest = SubmitOrderRequestBuilder.build(confProperties);
        assertNotNull(submitOrderRequest);
        assertNotNull(submitOrderRequest.getContext());
    }
}
