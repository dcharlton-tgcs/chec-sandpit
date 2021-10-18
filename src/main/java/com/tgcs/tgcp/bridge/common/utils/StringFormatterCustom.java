package com.tgcs.tgcp.bridge.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class StringFormatterCustom {

    /**
     * @param template
     * @param params
     * @return
     */
    public static String replaceParams(String template, Map<String, String> params) {
        String result = template;
        for (String key : params.keySet()) {
            result = result.replace("{" + key + "}", params.get(key));
        }
        return result;
    }

    public static String createUriEndpointFromTemplate(String template, Map<String, String> params) {
        String endpoint = replaceParams(template, params);
        endpoint = endpoint.replaceAll("(/)\\1+","/");
        return endpoint;
    }

    public static String createPaddingString(char fillingChar, int length) {
        char[] emptySpaces = new char[length];
        Arrays.fill(emptySpaces, fillingChar);

        return new String(emptySpaces);

    }

    public static String getJsonValueForKey(String json, String message) throws IOException {
        Map<String, String> msgVariablesMap = new ObjectMapper().readValue(json, Map.class);
        return msgVariablesMap.get(message);
    }


}
