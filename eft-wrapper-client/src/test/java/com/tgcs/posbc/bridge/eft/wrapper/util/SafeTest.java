package com.tgcs.posbc.bridge.eft.wrapper.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class SafeTest {

    @Test
    public void testFormatTPNetAmount_checFormat() {
        assertEquals("Chec Format #.##", 200, Safe.formatTPNetAmount("2.00").longValue());
        assertEquals("Other locale #,##", 400, Safe.formatTPNetAmount("4,00").longValue());
        assertEquals("Integer #", 500, Safe.formatTPNetAmount("5").longValue());
    }

    @Test
    public void testIsPaidAmountEqualsRequestedAmount() {
        assertEquals("   1 == 0.01", 1, Safe.formatTPNetAmount("0.01").longValue());
        assertEquals(" 100 == 1.00", 100,  Safe.formatTPNetAmount("1.00").longValue());
        assertEquals("1000 == 10,00", 1000,  Safe.formatTPNetAmount("10,00").longValue());
    }
}
