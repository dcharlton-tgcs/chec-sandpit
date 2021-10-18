package com.tgcs.tgcp.bridge.common.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StringFormatterCustomTest {

    @Test
    public void createPaddingStringTest(){
        assertEquals(".....", StringFormatterCustom.createPaddingString('.', 5));
        assertEquals(5, StringFormatterCustom.createPaddingString('.', 5).length());
    }

    @Test
    public void createPaddingStringWrongValueTest(){
        assertNotEquals("...aa", StringFormatterCustom.createPaddingString('.', 5));
    }

    @Test
    public void createUriEndpointFromTemplate(){
        String test = "AddItem/{key1}/{key2}";
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertNotEquals("AddItem/{value1}/{value2}", StringFormatterCustom.createUriEndpointFromTemplate(test, map));

    }

}