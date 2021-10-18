package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.CompleteOrderRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfProperties.class})
public class CompleteOrderRequestBuilderTest {

    @Autowired
    ConfProperties confProperties;

    @Test
    public void testCompleteOrderRequestNotNull() {
        CompleteOrderRequest completeOrderRequest = CompleteOrderRequestBuilder.build(confProperties);
        assertNotNull(completeOrderRequest);
    }
}
