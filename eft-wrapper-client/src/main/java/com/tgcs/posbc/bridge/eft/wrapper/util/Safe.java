package com.tgcs.posbc.bridge.eft.wrapper.util;

import java.text.NumberFormat;

/**
 * Helper class for Eft wrapper client
 */
public class Safe {

    /**
     * Utility class to flatten a json String object on single line.
     * Used Log for loggin
     * @param value json Object string
     * @return the json Object string cleaned of carriage return, and unnecessary tab and space
     */
    public static String singleLineJsonString(String value ) {
        return value
                .replaceAll("(\":|\"?,|\\{)[\\s ]+", "$1")
                .replaceAll("[\r\n]", "");
    }

    /**
     * Utility class to transform string amount to TP.net integer amount.
     * It handle any possibility of locale separators : dot, comma, space
     *
     * examples :
     *      "1" return 100
     *      "2.00" return 1200
     *      "3,00" return 300
     *
     * @param requestedAmount an amount String with decimal format #.##
     * @return the integer amount padded with the 2 decimal
     */
    public static Integer formatTPNetAmount(String requestedAmount) {
        String prepare = requestedAmount.replaceAll(",", ".").replaceAll(" ", "");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(1);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String clean = nf.format(Float.parseFloat(prepare)).replaceAll("[^0-9]", "");
        return Integer.valueOf(clean);
    }

}
