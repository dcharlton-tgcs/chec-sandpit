package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.adapter.Item;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
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
public class BarcodeRequestBuilderTest {

    @Autowired
    ConfProperties confProperties;

    @Test
    public void testNullRequest() {
        BarcodeRequest barcodeRequest = BarcodeRequestBuilder.build(new Item(), confProperties);
        assertNull(barcodeRequest);
    }

    @Test
    public void testBuildRequest() {
        BarcodeRequest barcodeRequest = BarcodeRequestBuilder.build(
                new OrderItem()
                        .setItemIdentifier("111")
                        .setScanDataType("SCAN_SDT_EAN13")
                        .setQuantity(new BigDecimal(1)),
                confProperties);
        assertNotNull(barcodeRequest);
        assertNotNull(barcodeRequest.getContext());
        assertEquals("111", barcodeRequest.getBarcodeData());
        assertEquals("SCAN_SDT_EAN13", barcodeRequest.getSymbology().toString());
        assertEquals(new BigDecimal(1), barcodeRequest.getQuantity());
    }
}