package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.adapter.PaymentInfo;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.AddPaymentRequest;
import com.tgcs.tgcp.pos.model.PaymentTypeGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfProperties.class})
public class AddPaymentRequestBuilderTest {

    @SpyBean
    ConfProperties propertiesSpy;

    @MockBean
    PaymentInfo paymentInfo;

    @Before
    public void setUp() {
        doReturn(PaymentInfo.PaymentType.CASH.value).when(paymentInfo).getPaymentType();
        doReturn(PaymentInfo.PaymentTypeGroup.CASH).when(paymentInfo).getPaymentTypeGroup();
        doReturn("1.00").when(paymentInfo).getAmount();

        ReflectionTestUtils.setField(propertiesSpy, "posHandleEft", false);
    }

    @Test
    public void testBuildRequestCASH() {
        AddPaymentRequest addPaymentRequest = AddPaymentRequestBuilder.build(paymentInfo, propertiesSpy, "EUR");

        assertNotNull(addPaymentRequest);
        assertNotNull(addPaymentRequest.getContext());

        assertNotNull(addPaymentRequest.getPaymentEntryData());
        assertEquals("CASH_EUR", addPaymentRequest.getPaymentEntryData().getType());
        assertEquals(PaymentTypeGroup.CASH, addPaymentRequest.getPaymentEntryData().getGroup());
        assertNull(addPaymentRequest.getPaymentEntryData().getAccountNumber());
        assertNull(addPaymentRequest.getPaymentEntryData().getPinData());
        assertNull(addPaymentRequest.getPaymentEntryData().getDisplayAccountNumber());
        assertNull(addPaymentRequest.getPaymentEntryData().getExpirationDate());
        assertNull(addPaymentRequest.getPaymentEntryData().getEntryMethod());
        assertNull(addPaymentRequest.getPaymentEntryData().getCustomerName());

        assertNotNull(addPaymentRequest.getPaymentTransaction());

        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount());
        assertEquals(new BigDecimal("1.00"), addPaymentRequest.getPaymentTransaction().getRequestedAmount().getValue());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyCode());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyValue());
        assertEquals(new BigDecimal("1.00"), addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyValue().getValue());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyCode());

    }

    @Test
    public void testBuildRequestDebit() {
        doReturn(PaymentInfo.PaymentType.DEBIT.value).when(paymentInfo).getPaymentType();
        doReturn(PaymentInfo.PaymentTypeGroup.DEBIT).when(paymentInfo).getPaymentTypeGroup();

        AddPaymentRequest addPaymentRequest = AddPaymentRequestBuilder.build(paymentInfo, propertiesSpy, "EUR");

        assertNotNull(addPaymentRequest);
        assertNotNull(addPaymentRequest.getContext());

        assertNotNull(addPaymentRequest.getPaymentEntryData());
        assertEquals("DEBIT", addPaymentRequest.getPaymentEntryData().getType());
        assertEquals(PaymentTypeGroup.DEBIT, addPaymentRequest.getPaymentEntryData().getGroup());
        assertNotNull(addPaymentRequest.getPaymentEntryData().getAccountNumber());
        assertNotNull(addPaymentRequest.getPaymentEntryData().getPinData());
        assertNotNull(addPaymentRequest.getPaymentEntryData().getDisplayAccountNumber());
        assertNotNull(addPaymentRequest.getPaymentEntryData().getExpirationDate());
        assertNotNull(addPaymentRequest.getPaymentEntryData().getEntryMethod());
        assertNotNull(addPaymentRequest.getPaymentEntryData().getCustomerName());

        assertNotNull(addPaymentRequest.getPaymentTransaction());

        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount());
        assertEquals(new BigDecimal("1.00"), addPaymentRequest.getPaymentTransaction().getRequestedAmount().getValue());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyCode());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyValue());
        assertEquals(new BigDecimal("1.00"), addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyValue().getValue());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyCode());

    }

    @Test
    public void testBuildRequestMisc() {
        PaymentInfo paymentInfo = new PaymentInfo()
                .setAmount("1.00")
                .setPaymentType("Misc")
                .setPaymentTypeGroup(PaymentInfo.PaymentTypeGroup.OTHER)
                .setAdditional(Collections.singletonMap("Key", "Value"));

        AddPaymentRequest addPaymentRequest = AddPaymentRequestBuilder.build(paymentInfo, propertiesSpy, "EUR");

        assertNotNull(addPaymentRequest);
        assertNotNull(addPaymentRequest.getContext());

        assertNotNull(addPaymentRequest.getPaymentEntryData());
        assertEquals("Misc", addPaymentRequest.getPaymentEntryData().getType());
        assertEquals(PaymentTypeGroup.OTHER, addPaymentRequest.getPaymentEntryData().getGroup());
        assertNotNull(addPaymentRequest.getAdditional());
        assertEquals("Value", addPaymentRequest.getPaymentEntryData().getAdditional().get("Key"));

        assertNotNull(addPaymentRequest.getPaymentTransaction());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount());
        assertEquals(new BigDecimal("1.00"), addPaymentRequest.getPaymentTransaction().getRequestedAmount().getValue());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyCode());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyValue());
        assertEquals(new BigDecimal("1.00"), addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyValue().getValue());
        assertNotNull(addPaymentRequest.getPaymentTransaction().getRequestedAmount().getCurrencyCode());
    }

    @Test(expected = NullPointerException.class)
    public void testBuildRequestFailed() {
        AddPaymentRequestBuilder.build(new PaymentInfo(), propertiesSpy, "EUR");
    }

}