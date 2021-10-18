package com.tgcs.posbc.bridge.eft.wrapper.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = {
        EftWrapperAutoConfiguration.class
})
public class EftWrapperPropertiesTest {

    @Autowired
    private EftWrapperProperties properties;

    @Test
    public void testEftWrapperProperties() {
        assertEquals("/CHECVic107WrapperSimulator", properties.getContextPath());
        assertEquals("http", properties.getProto());
        assertEquals("192.168.1.150", properties.getHost());
        assertEquals(8181, properties.getPort().longValue());
        assertEquals(5, properties.getConnectTimeout().longValue());
        assertEquals(2, properties.getReadTimeout().longValue());
    }
}
