package com.tgcs.posbc.bridge.eft.wrapper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgcs.posbc.bridge.eft.wrapper.config.EftWrapperProperties;
import com.tgcs.posbc.bridge.eft.wrapper.exception.*;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftResponse;
import com.tgcs.posbc.bridge.eft.wrapper.util.Matchers;
import com.tgcs.posbc.bridge.eft.wrapper.util.Safe;
import com.tgcs.posbc.bridge.eft.wrapper.util.TestSupport;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = {
        EftWrapperClientImpl.class,
        ObjectMapper.class,
        TestSupport.class
})
@EnableConfigurationProperties({EftWrapperProperties.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EftWrapperClientTest
{
    @Autowired
    private EftWrapperClient client;

    @Autowired
    private EftWrapperProperties properties;

    @Autowired
    private TestSupport testSupport;

    @MockBean
    public RestTemplate restTemplate;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testOpen() {
        doReturn(testSupport.getStringResponse("responses/success_open.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        EftResponse response = client.open(testSupport.createEftContext());

        assertNotNull(response);
        assertEquals("Code Ok", 0L, response.getResultCode().longValue());
        assertEquals("Message Ok", "", response.getDspText());
    }

    @Test
    public void testReprint_success() {
        doReturn(testSupport.getStringResponse("responses/success_reprint.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        EftResponse response = client.reprint(testSupport.createEftContext());

        assertNotNull(response);
        assertEquals("Code Ok", 0L, response.getResultCode().longValue());
        assertEquals("AmountPaid Ok", 1L, response.getAmountPaid().longValue());
        assertEquals("Message Ok", "Reprint", response.getDspText());
    }

    @Test
    public void testReprint_notOpen() {
        expectedException.expect(EftException.class);
        expectedException.expect(Matchers.isDeviceClosed());

        doReturn(testSupport.getStringResponse("responses/fail_not_open.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.reprint(testSupport.createEftContext());
    }

    @Test
    public void testDebitPayment_accepted() {
        doReturn(testSupport.getStringResponse("responses/success_debit.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        EftResponse response = client.debitPayment(testSupport.createEftContext(), "2.00");

        assertNotNull(response);
        assertEquals(0, (long)response.getResultCode());
        assertEquals("Amount Ok", response.getAmountPaid(), Safe.formatTPNetAmount("2.00"));
        assertTrue("Test Ok", response.getCusTicket().startsWith("Text to print\\nPayed"));
    }

    @Test
    public void testDebitPayment_notOpen() {
        expectedException.expect(EftException.class);
        expectedException.expect(Matchers.isDeviceClosed());

        doReturn(testSupport.getStringResponse("responses/fail_not_open.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.debitPayment(testSupport.createEftContext(), "2.00");
    }

    @Test
    public void testDebitPayment_needReprint() {
        expectedException.expect(ReprintNeededException.class);

        doReturn(testSupport.getStringResponse("responses/fail_need_reprint.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.debitPayment(testSupport.createEftContext(), "2.01");
    }

    @Test
    public void testDebitPayment_refused() {
        expectedException.expect(EftException.class);
        expectedException.expect(Matchers.isTransactionRefused());

        doReturn(testSupport.getStringResponse("responses/fail_trans_refused.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.debitPayment(testSupport.createEftContext(), "2.01");
    }

    @Test
    public void testDebitPayment_requestFail() {
        expectedException.expect(EftRequestException.class);
        expectedException.expectMessage(String.format(EftRequestException.MESSAGE, "Nok"));

        doThrow(new RuntimeException("Nok"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.debitPayment(testSupport.createEftContext(), "2.00");
    }

    @Test
    public void testDebitPayment_badResponse() {
        expectedException.expect(EftRequestException.class);

        doReturn(null)
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.debitPayment(testSupport.createEftContext(), "2.00");
    }

    @Test
    public void testDebitPayment_timeout() {
        expectedException.expect(EftTimeoutException.class);
        String url = properties.urlBuilder().pathSegment("debit").build().toString();

        doThrow(new ResourceAccessException("anyString", new SocketTimeoutException()))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        client.debitPayment(testSupport.createEftContext(), "2.01");
    }

    @Test
    public void testClose() {
        doReturn(testSupport.getStringResponse("responses/success_close.json"))
                .when(restTemplate)
                .postForObject(anyString(), any(HttpEntity.class), any());

        EftResponse response = client.close(testSupport.createEftContext());

        assertNotNull(response);
        assertEquals(0L, (long)response.getResultCode());
    }

}
