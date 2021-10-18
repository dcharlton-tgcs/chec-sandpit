package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfProperties.class})
public class ContextBuilderTest {

    @Autowired
    ConfProperties confProperties;

    @Test
    public void testContextNotNull() {
        Context context = ContextBuilder.build(confProperties);
        assertNotNull(context);
    }
}