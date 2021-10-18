//package com.tgcs.posbc.bridge.eft.wrapper.client;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tgcs.posbc.bridge.eft.wrapper.config.EftWrapperAutoConfiguration;
//import com.tgcs.posbc.bridge.eft.wrapper.config.EftWrapperProperties;
//import com.tgcs.posbc.bridge.eft.wrapper.exception.ClosedDeviceException;
//import com.tgcs.posbc.bridge.eft.wrapper.exception.ReprintNeededException;
//import com.tgcs.posbc.bridge.eft.wrapper.model.EftResponse;
//import com.tgcs.posbc.bridge.eft.wrapper.util.Safe;
//import com.tgcs.posbc.bridge.eft.wrapper.util.TestSupport;
//import org.junit.FixMethodOrder;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Profile;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@ContextConfiguration(classes = {
//        EftWrapperAutoConfiguration.class,
//        ObjectMapper.class,
//        TestSupport.class
//})
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class EftWrapperClientIntegrationTest {
//
//    @Autowired
//    private EftWrapperClient client;
//
//    @Autowired
//    private TestSupport testSupport;
//
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//
//    @Test
//    public void $01_testReprint_notOpen() {
//        expectedException.expect(ClosedDeviceException.class);
//
//        client.close(null);
//        client.reprint(testSupport.createEftContext());
//    }
//
//    @Test
//    public void $02_testDebitPayment_notOpen() {
//        expectedException.expect(ClosedDeviceException.class);
//
//        client.close(null);
//        client.debitPayment(testSupport.createEftContext(), "2.00");
//    }
//
//    @Test
//    public void $03_testOpen() {
//        EftResponse response = client.open(testSupport.createEftContext());
//
//        assertNotNull(response);
//        assertEquals(0L, (long)response.getResultCode());
//    }
//
//    @Test
//    public void $04_testReprint_success() {
//        EftResponse response = client.reprint(testSupport.createEftContext());
//
//        assertNotNull(response);
//        assertEquals("Message Ok", "Reprint", response.getDspText());
//    }
//
//    @Test
//    public void $05_testDebitPayment_success() {
//        EftResponse response = client.debitPayment(testSupport.createEftContext(), "2.00");
//
//        assertNotNull(response);
//        assertEquals(0, (long)response.getResultCode());
//        assertEquals("Amount Ok", response.getAmountPaid(), Safe.formatTPNetAmount("2.00"));
//        assertTrue("Test Ok", response.getCusTicket().startsWith("Text to print\\nPayed"));
//    }
//
//    @Test
//    public void $06_testDebitPayment_fail() {
//        expectedException.expect(ReprintNeededException.class);
//
//        client.debitPayment(testSupport.createEftContext(), "2.01");
//    }
//
//    @Test
//    public void $07_testDebitPayment_needReprint() {
//        expectedException.expect(ReprintNeededException.class);
//
//        client.debitPayment(testSupport.createEftContext(), "2.01");
//    }
//
//    @Test
//    public void $08_testClose() {
//        EftResponse response = client.close(testSupport.createEftContext());
//
//        assertNotNull(response);
//        assertEquals(0L, (long)response.getResultCode());
//    }
//}
