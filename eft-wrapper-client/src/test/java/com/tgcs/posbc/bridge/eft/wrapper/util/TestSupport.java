package com.tgcs.posbc.bridge.eft.wrapper.util;

import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component(TestSupport.BEAN_NAME)
public class TestSupport {
    public static final String BEAN_NAME = "TestSupport";

    public String getStringResponse(String filename) {
        String response = null;
        try {
            response = readFileToString(filename);
        } catch (IOException e) {
            Assert.fail("IOException: " + e);
        }
        Assert.assertNotNull(response);
        return response;
    }

    public static String readFileToString(String filename) throws IOException {
        String retVal = null;
        try {
            retVal = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(filename).toURI())));
        } catch (NullPointerException | URISyntaxException e) {
            Assert.fail("URI syntax error: " + e);
        }

        return retVal;
    }

    public EftContext createEftContext() {
        EftContext context = new EftContext();
        context.setCasherId("201");
        context.setEndpointId("201");
        context.setRequestId("227");
        return context;
    }

}
