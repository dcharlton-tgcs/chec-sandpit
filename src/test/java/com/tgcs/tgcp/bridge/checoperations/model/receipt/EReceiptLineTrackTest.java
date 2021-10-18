package com.tgcs.tgcp.bridge.checoperations.model.receipt;


import com.tgcs.tgcp.bridge.adapter.OrderItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
public class EReceiptLineTrackTest {

    EReceiptLineTrack eReceiptLineTrack;

    @Before
    public void setUp() {
        eReceiptLineTrack = new EReceiptLineTrack();
        eReceiptLineTrack.put(new EReceiptLine("1234567890123", "1", new OrderItem()));
        eReceiptLineTrack.setHeaderPrinted(true);
    }

    @Test
    public void testGetLineInfoByLineContent() {

        EReceiptLine eReceiptLine = eReceiptLineTrack.getLineInfoByLineContent("1234567890123");

        assertNotNull(eReceiptLine);
        assertEquals("1234567890123", eReceiptLine.getLineContent());
        assertEquals("1", eReceiptLine.getLineId());
    }

    @Test
    public void testGetLineInfoByLineContentNullReceiptLine() {
        assertNull(eReceiptLineTrack.getLineInfoByLineContent("9999999999999"));
    }

    @Test
    public void testGetLineInfoByLineId() {

        EReceiptLine eReceiptLine = eReceiptLineTrack.getLineInfoByLineId("1");

        assertNotNull(eReceiptLine);
        assertEquals("1234567890123", eReceiptLine.getLineContent());
        assertEquals("1", eReceiptLine.getLineId());
    }

    @Test
    public void testGetLineInfoByLineIdNullReceiptLine() {

        assertNull(eReceiptLineTrack.getLineInfoByLineId("2"));
    }

    @Test
    public void testPutIfAbsent() {

        EReceiptLine eReceiptLine = new EReceiptLine("666", "666", new OrderItem());

        eReceiptLineTrack.putIfAbsent(eReceiptLine);

        EReceiptLine eReceiptLineResult = eReceiptLineTrack.getLineInfoByLineId("666");

        assertNotNull(eReceiptLineResult);
        assertEquals(2, eReceiptLineTrack.getReceiptLines().size());
    }

    @Test
    public void testPutIfAbsentOriginalLine() {

        EReceiptLine eReceiptLine = new EReceiptLine("1234567890123", "1", new OrderItem());

        eReceiptLineTrack.putIfAbsent(eReceiptLine);

        EReceiptLine eReceiptLineResult = eReceiptLineTrack.getLineInfoByLineId("1");

        assertNotNull(eReceiptLineResult);
        assertEquals(1, eReceiptLineTrack.getReceiptLines().size());
    }

    @Test
    public void testPut() {

        EReceiptLine eReceiptLine = new EReceiptLine("1234567890123", "1", new OrderItem());

        eReceiptLineTrack.put(eReceiptLine);
        assertEquals(2, eReceiptLineTrack.getReceiptLines().size());
    }

    @Test
    public void testContainsContent() {
        assertTrue(eReceiptLineTrack.containsContent("1234567890123"));
    }

    @Test
    public void testContainsContentNoLine() {
        assertFalse(eReceiptLineTrack.containsContent("2"));
    }


    @Test
    public void testUpdateLineIdForContent() {

        EReceiptLine eReceiptLine = new EReceiptLine("3", "3", new OrderItem());

        eReceiptLineTrack.putIfAbsent(eReceiptLine);

        eReceiptLineTrack.updateLineIdForContent("3", "666");

        assertEquals("666", eReceiptLineTrack.getLineInfoByLineContent("3").getLineId());
    }

    @Test
    public void testRemoveLineByContent() {

        eReceiptLineTrack.removeLineByContent("1234567890123");

        assertEquals(0, eReceiptLineTrack.getReceiptLines().size());
    }

    @Test
    public void testRemoveLineByContentNoLine() {

        eReceiptLineTrack.removeLineByContent("2");

        assertEquals(1, eReceiptLineTrack.getReceiptLines().size());
    }

    @Test
    public void testGetReceiptLines() {

        EReceiptLine eReceiptLine = new EReceiptLine("4444444444444", "4", new OrderItem());

        eReceiptLineTrack.putIfAbsent(eReceiptLine);

        List<EReceiptLine> eReceiptLineList = eReceiptLineTrack.getReceiptLines();

        assertNotNull(eReceiptLineList);
        assertEquals(2, eReceiptLineList.size());
    }

    @Test
    public void testIsHeaderPrinted() {

        assertTrue(eReceiptLineTrack.isHeaderPrinted());

    }

    @Test
    public void testSetHeaderPrinted() {
        eReceiptLineTrack.setHeaderPrinted(false);

        assertFalse(eReceiptLineTrack.isHeaderPrinted());


    }
}