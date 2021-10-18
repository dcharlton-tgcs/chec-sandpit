package com.tgcs.tgcp.bridge.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;
import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestSupport {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getObject(Class<T> tClass, String filename) {
        objectMapper.registerModule(new JavaTimeModule());
        T response = null;
        try {
            String str = readFileToString(filename);
            if (tClass.isAssignableFrom(String.class)) {
                response = (T) str;
            } else {
                response = objectMapper.readValue(str, tClass);
            }
        } catch (IOException e) {
            Assert.fail("IOException: " + e);
        }
        Assert.assertNotNull(response);
        return response;
    }

    public static String getString(String filename) {
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
