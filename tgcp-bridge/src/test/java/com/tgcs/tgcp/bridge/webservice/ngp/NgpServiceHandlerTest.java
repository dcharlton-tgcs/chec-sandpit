package com.tgcs.tgcp.bridge.webservice.ngp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgcs.posbc.bridge.eft.wrapper.client.EftWrapperClient;
import com.tgcs.posbc.bridge.eft.wrapper.exception.EftException;
import com.tgcs.posbc.bridge.eft.wrapper.exception.ReprintNeededException;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftResponse;
import com.tgcs.posbc.bridge.printer.model.ReceiptData;
import com.tgcs.tgcp.authorization.api.client.impl.AuthorizationClientImpl;
import com.tgcs.tgcp.authorization.api.service.AuthorizationService;
import com.tgcs.tgcp.authorization.api.service.AuthorizationServiceClient;
import com.tgcs.tgcp.bridge.adapter.*;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.print.PrinterHandler;
import com.tgcs.tgcp.bridge.util.TestSupport;
import com.tgcs.tgcp.bridge.webservice.WebServiceRequest;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel.OrderResponse;
import com.tgcs.tgcp.cash.management.api.client.impl.CashManagementClientImpl;
import com.tgcs.tgcp.cash.management.api.service.CashManagementService;
import com.tgcs.tgcp.cash.management.model.Till;
import com.tgcs.tgcp.framework.core.error.ClientErrorResponseException;
import com.tgcs.tgcp.framework.spring.client.*;
import com.tgcs.tgcp.pos.api.service.PosService;
import com.tgcs.tgcp.pos.model.Order;
import com.tgcs.tgcp.pos.tpnet.service.impl.PosTpnetServiceImpl;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SpringRestfulAPIHeaders.class, SessionTokensUtil.class, ObjectMapper.class,
        AuthorizationServiceClient.class, AuthorizationClientImpl.class, NgpServiceHandler.class,
        PosService.class, PosTpnetServiceImpl.class, CashManagementClientImpl.class,
        SpringRestfulAPIAuthHeadersUtil.class, SpringClientRestfulAPIClientUtil.class,
        RestTemplate.class, WebServiceRequest.class})
@SpringBootTest(classes = {ConfProperties.class})
public class NgpServiceHandlerTest {

    @Autowired
    NgpServiceHandler ngpServiceHandler;

    @SpyBean
    NgpSession ngpSession;

    @MockBean
    PaymentInfo paymentInfo;

    @MockBean
    ResponseEntity<OrderResponse> responseEntity;

    @MockBean
    OrderResponse ngpOrder;

    @MockBean
    Till till;

    NgpServiceHandler ngpServiceHandlerSpy;

    @SpyBean
    NgpSessionManager ngpSessionManager;

    @MockBean
    Order order;

    @MockBean
    PosService posService;

    @MockBean
    CashManagementClientImpl cashManagementClient;

    @MockBean
    OrderItem orderItem;

    @MockBean
    OrderCustomer orderCustomer;

    @SpyBean
    ConfProperties propertiesSpy;

    @MockBean
    AuthorizationServiceClient authorizationServiceClient;

    @MockBean
    SpringRestfulAPIHeaders headers;

    @MockBean
    HttpHeaders httpHeaders;

    @MockBean
    EftWrapperClient eftWrapperClient;

    @MockBean
    PrinterHandler printerHandler;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        ngpServiceHandlerSpy = spy(ngpServiceHandler);

        when(responseEntity.getBody()).thenReturn(ngpOrder);
        when(ngpSession.getOrderResponse()).thenReturn(ngpOrder);

        when(ngpOrder.getTotal()).thenReturn(new BigDecimal("5"));
        when(ngpOrder.getTotalBeforeTaxes()).thenReturn(new BigDecimal("4"));
        when(ngpOrder.getTotalTaxes()).thenReturn(new BigDecimal("1"));
        when(ngpOrder.getTotalItemCount()).thenReturn(new BigDecimal("1"));
        when(ngpOrder.getTotalItemCount()).thenReturn(new BigDecimal("1"));
        when(ngpOrder.getTotalPayments()).thenReturn(new BigDecimal("5"));
        when(ngpOrder.getPaymentShortage()).thenReturn(new BigDecimal("0"));

        when(ngpSession.getNgpOrderResponse()).thenReturn(order);
        when(ngpSession.getCurrencyCode()).thenReturn("EUR");
        when(order.getId()).thenReturn("1");
        when(order.getVersion()).thenReturn((long) 1);
        when(order.getCreateTimestamp()).thenReturn(OffsetDateTime.now());
        when(order.getLastModifiedTimestamp()).thenReturn(OffsetDateTime.now());

        when(orderItem.getItemIdentifier()).thenReturn("111");
        when(orderItem.getQuantity()).thenReturn(new BigDecimal(1));

        when(orderCustomer.getCustomerId()).thenReturn("2622166053300");

        ReflectionTestUtils.setField(propertiesSpy, "cashManagementEnabled", true);
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");
        ReflectionTestUtils.setField(propertiesSpy, "posHandlePrinter", true);


        when(cashManagementClient.openTill(any(CashManagementService.openTillBuilder.class))).thenReturn(till);
        when(posService.tpnetSignOn(any(PosService.tpnetSignOnBuilder.class))).thenReturn(new Object());
        when(headers.getGlobalRequestHeaders()).thenReturn(httpHeaders);
        when(headers.getResponseHeaders()).thenReturn(httpHeaders);
    }

    @Test
    public void testInitializeWebServiceTpnetWithPrinter() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");

        when(eftWrapperClient.open(any())).thenReturn(TestSupport.getObject(EftResponse.class, "eft-responses/success.json"));
        when(posService.tpnetSignOn(any())).thenReturn(null);

        ngpServiceHandlerSpy.initializeWebService();

        verify(ngpSessionManager).execute(any());
        verify(posService).tpnetSignOn(any(PosService.tpnetSignOnBuilder.class));
        verify(cashManagementClient).openTill(any());
        verify(authorizationServiceClient, times(0)).authorizationLogin(any(AuthorizationService.authorizationLoginBuilder.class));
        verify(ngpSession).setCurrencyCode("EUR");
    }

    @Test
    public void testInitializeWebServiceTpnetNoPrinter() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");
        ReflectionTestUtils.setField(propertiesSpy, "posHandlePrinter", false);

        when(eftWrapperClient.open(any())).thenReturn(TestSupport.getObject(EftResponse.class, "eft-responses/success.json"));
        when(posService.tpnetSignOn(any())).thenReturn(null);

        ngpServiceHandlerSpy.initializeWebService();

        verify(ngpSession, times(0)).getNgpOrderResponse();
        verify(ngpSessionManager).execute(any());
        verify(posService).tpnetSignOn(any(PosService.tpnetSignOnBuilder.class));
        verify(cashManagementClient).openTill(any());
        verify(authorizationServiceClient, times(0)).authorizationLogin(any(AuthorizationService.authorizationLoginBuilder.class));
        verify(ngpSession).setCurrencyCode("EUR");
    }

    @Test
    public void testInitializeWebServiceTpnetException() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");
        expectedException.expect(ClientErrorResponseException.class);

        doThrow(ClientErrorResponseException.class).when(posService).tpnetSignOn(any(PosService.tpnetSignOnBuilder.class));

        ngpServiceHandlerSpy.initializeWebService();

    }

    @Test
    public void testInitializeWebServiceNgp() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "ngp");
        when(authorizationServiceClient.authorizationLogin(any(AuthorizationService.authorizationLoginBuilder.class))).thenReturn(new Object());

        ngpServiceHandlerSpy.initializeWebService();

        verify(posService, times(0)).tpnetSignOn(any(PosService.tpnetSignOnBuilder.class));
        verify(ngpSessionManager).execute(any());
        verify(cashManagementClient).openTill(any(CashManagementClientImpl.openTillBuilder.class));
        verify(authorizationServiceClient).authorizationLogin(any(AuthorizationService.authorizationLoginBuilder.class));
        verify(ngpSession).setCurrencyCode("EUR");
    }

    @Test
    public void testInitializeWebServiceNgpWithAuthorization() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "ngp");
        when(httpHeaders.containsKey("authorization")).thenReturn(true);

        ngpServiceHandlerSpy.initializeWebService();

        verify(ngpSessionManager).execute(any());
        verify(cashManagementClient).openTill(any(CashManagementClientImpl.openTillBuilder.class));
        assertTrue(httpHeaders.containsKey("authorization"));
        verify(authorizationServiceClient, times(0)).authorizationLogin(any(AuthorizationService.authorizationLoginBuilder.class));
        verify(ngpSession).setCurrencyCode("EUR");
    }

    @Test
    public void testInitializeWebServiceWithCashManagementDisabled() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "cashManagementEnabled", false);

        ngpServiceHandlerSpy.initializeWebService();

        verify(ngpSessionManager, times(0)).execute(any());
    }

    @Test
    public void testOpenTillAlreadyAssigned() throws Exception {
        String message = new JSONObject().put("message", "A primary till is already assigned to the endpoint in context").toString();
        ErrorResponseException errorResponseException = new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        doThrow(errorResponseException).when(cashManagementClient).openTill(any(CashManagementService.openTillBuilder.class));
        when(httpHeaders.containsKey("authorization")).thenReturn(true);

        ngpServiceHandlerSpy.initializeWebService();

        verify(ngpSessionManager).execute(any());
        verify(cashManagementClient).openTill(any(CashManagementClientImpl.openTillBuilder.class));
        assertTrue(httpHeaders.containsKey("authorization"));
        verify(authorizationServiceClient, times(0)).authorizationLogin(any(AuthorizationService.authorizationLoginBuilder.class));
        verify(ngpSession).setCurrencyCode("EUR");
    }

    @Test
    public void testOpenedTillThrowsException() throws Exception {
        expectedException.expect(WsErrorResponse.class);
        String message = new JSONObject().put("message", "Server Error Processing Request").toString();
        doThrow(new ErrorResponseException(HttpStatus.BAD_REQUEST.value(), message)).when(cashManagementClient).openTill(any(CashManagementService.openTillBuilder.class));
        ngpServiceHandlerSpy.initializeWebService();
    }

    @Test
    public void testAddFirstItem() throws Exception {
        when(ngpSession.getNgpOrderResponse()).thenReturn(null).thenReturn(order);
        when(posService.addBarcodeCreateOrder(any(PosService.addBarcodeCreateOrderBuilder.class))).thenReturn(order);
        ReflectionTestUtils.setField(propertiesSpy, "posHandlePrinter", false);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.addItem(orderItem);

        verify(ngpSession, times(3)).getNgpOrderResponse();
        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).addBarcodeCreateOrder(any(PosService.addBarcodeCreateOrderBuilder.class));
    }

    @Test
    public void testAddSecondItem() throws Exception {
        when(order.getTotalItemCount()).thenReturn(new BigDecimal(1));
        when(posService.addBarcodeToOrder(any(PosService.addBarcodeToOrderBuilder.class))).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.addItem(orderItem);

        verify(ngpSession, times(5)).getNgpOrderResponse();
        verify(ngpSession.getNgpOrderResponse()).getTotalItemCount();
        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).addBarcodeToOrder(any(PosService.addBarcodeToOrderBuilder.class));
    }

    @Test(expected = Exception.class)
    public void testAddItemNotInitialized() throws Exception {
        ngpServiceHandlerSpy.addItem(orderItem);
    }

    @Test
    public void testAddPaymentNgp() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "ngp");
        when(posService.addPaymentToOrder(any(PosService.addPaymentToOrderBuilder.class))).thenReturn(order);

        doReturn(PaymentInfo.PaymentType.CASH.value).when(paymentInfo).getPaymentType();
        doReturn(PaymentInfo.PaymentTypeGroup.CASH).when(paymentInfo).getPaymentTypeGroup();
        doReturn("1.00").when(paymentInfo).getAmount();

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.addPayment(paymentInfo);

        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).addPaymentToOrder(any(PosService.addPaymentToOrderBuilder.class));
    }

    @Test
    public void testAddPaymentTpnet() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");

        String amount = "4.50";
        BigDecimal amountDecimal = new BigDecimal(amount).setScale(2);

        when(order.getTotal()).thenReturn(amountDecimal);
        when(ngpSession.getCurrencyCode()).thenReturn("EUR");

        when(posService.addPaymentToOrder(any(PosService.addPaymentToOrderBuilder.class))).thenReturn(order);
        when(posService.tpnetSignOn(any(PosService.tpnetSignOnBuilder.class))).thenReturn(null);

        when(paymentInfo.getAmount()).thenReturn(amount);
        when(paymentInfo.getPaymentType()).thenReturn(PaymentInfo.PaymentType.DEBIT.value);
        when(paymentInfo.getPaymentTypeGroup()).thenReturn(PaymentInfo.PaymentTypeGroup.DEBIT);

        when(eftWrapperClient.open(any())).thenReturn(TestSupport.getObject(EftResponse.class, "eft-responses/success.json"));
        when(eftWrapperClient.debitPayment(any(), any())).thenReturn(TestSupport.getObject(EftResponse.class, "eft-responses/debit-success.json"));

        ngpServiceHandler.initializeWebService();
        OrderAdapter orderAdapter = ngpServiceHandler.addPayment(paymentInfo);

        assertNotNull(orderAdapter);
        assertEquals(amountDecimal, orderAdapter.getTotal());
    }

    @Test
    public void addPaymentNotInaTransactionTest() throws Exception {
        expectedException.expect(Exception.class);
        ngpServiceHandlerSpy.addPayment(paymentInfo);
    }

    @Test
    public void testAddPaymentNgpFailure() throws Exception {
        expectedException.expect(ErrorResponseException.class);
        ReflectionTestUtils.setField(propertiesSpy, "posType", "ngp");

        doThrow(ErrorResponseException.class).when(ngpSessionManager).execute(any());
        ngpServiceHandler.initializeWebService();
        ngpServiceHandlerSpy.addPayment(paymentInfo);
    }

    @Test
    public void testAddPaymentTpnetFailure() throws Exception {
        expectedException.expect(EftException.class);

        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");

        when(eftWrapperClient.open(any())).thenReturn(TestSupport.getObject(EftResponse.class, "eft-responses/success.json"));
        when(eftWrapperClient.debitPayment(any(), any())).thenThrow(EftException.class);
        doReturn(PaymentInfo.PaymentTypeGroup.DEBIT).when(paymentInfo).getPaymentTypeGroup();

        ngpServiceHandler.initializeWebService();
        ngpServiceHandler.addPayment(paymentInfo);
    }

    @Test
    public void testAddPaymentEftReprintNeededException() throws Exception {
        expectedException.expect(ReprintNeededException.class);

        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");

        when(eftWrapperClient.debitPayment(any(), any())).thenThrow(ReprintNeededException.class);
        doReturn(PaymentInfo.PaymentTypeGroup.DEBIT).when(paymentInfo).getPaymentTypeGroup();

        ngpServiceHandler.initializeWebService();
        ngpServiceHandler.addPayment(paymentInfo);
    }

    @Test
    public void testFinishTransaction() throws Exception {
        ngpServiceHandlerSpy.initializeWebService();
        when(posService.completeOrder(any(PosService.completeOrderBuilder.class))).thenReturn(order);
        when(posService.submitOrder(any(PosService.submitOrderBuilder.class))).thenReturn(order);
        assertTrue(ngpServiceHandlerSpy.finishTransaction());

        verify(ngpSessionManager, times(3)).execute(any());
        verify(ngpSession).setNgpOrderResponse(null);
    }

    @Test
    public void testFinishTransactionNotInitialized() throws Exception {
        when(ngpSession.getNgpOrderResponse()).thenReturn(null);
        assertFalse(ngpServiceHandlerSpy.finishTransaction());
        verify(ngpSessionManager, times(0)).execute(any());
        assertNull(ngpSession.getNgpOrderResponse());
    }

    @Test
    public void testFinishTransactionNotInATransactionWithCashMgmt() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "cashManagementEnabled", true);
        ngpServiceHandlerSpy.initializeWebService();
        when(ngpSession.getNgpOrderResponse()).thenReturn(null);
        assertFalse(ngpServiceHandlerSpy.finishTransaction());
        verify(ngpSessionManager, times(1)).execute(any());
        verify(ngpSession, times(0)).setNgpOrderResponse(null);
    }

    @Test
    public void testFinishTransactionNotInATransactionWithoutCashMgmt() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "cashManagementEnabled", false);
        ngpServiceHandlerSpy.initializeWebService();
        when(ngpSession.getNgpOrderResponse()).thenReturn(null);
        assertFalse(ngpServiceHandlerSpy.finishTransaction());
        verify(ngpSessionManager, times(0)).execute(any());
        verify(ngpSession, times(0)).setNgpOrderResponse(null);
    }

    @Test
    public void testFinishTransactionThrowsException() throws Exception {
        ngpServiceHandlerSpy.initializeWebService();
        doThrow(Exception.class).when(ngpSessionManager).execute(any());
        ngpServiceHandlerSpy.finishTransaction();
    }

    @Test
    public void testAddCustomerFirst() throws Exception {
        when(posService.addCustomerCreateOrder(any(PosService.addCustomerCreateOrderBuilder.class))).thenReturn(order);
        when(ngpSession.getNgpOrderResponse()).thenReturn(null).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.addCustomer(orderCustomer);

        verify(ngpSession, times(2)).getNgpOrderResponse();
        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).addCustomerCreateOrder(any(PosService.addCustomerCreateOrderBuilder.class));
    }

    @Test
    public void testAddCustomerSecond() throws Exception {
        when(order.getTotalItemCount()).thenReturn(new BigDecimal(1));
        when(posService.addCustomerToOrder(any(PosService.addCustomerToOrderBuilder.class))).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.addCustomer(orderCustomer);

        verify(ngpSession, times(4)).getNgpOrderResponse();
        verify(ngpSession.getNgpOrderResponse()).getTotalItemCount();
        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).addCustomerToOrder(any(PosService.addCustomerToOrderBuilder.class));
    }

    @Test
    public void addCustomerNotInitializedTest() throws Exception {
        expectedException.expect(Exception.class);
        ngpServiceHandlerSpy.addCustomer(new OrderCustomer());
    }

    @Test
    public void testVoidTransaction() throws Exception {
        when(posService.voidOrder(any(PosService.voidOrderBuilder.class))).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.voidTransaction();

        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).voidOrder(any(PosService.voidOrderBuilder.class));
        verify(ngpSession.getNgpOrderResponse()).getId();
        verify(ngpSession.getNgpOrderResponse()).getVersion();
    }

    @Test
    public void testVoidTransactionNotInitialized() throws Exception {
        expectedException.expect(Exception.class);
        ngpServiceHandlerSpy.voidTransaction();
    }

    @Test
    public void testVoidTransactionFailed() throws Exception {
        expectedException.expect(ErrorResponseException.class);
        doThrow(ErrorResponseException.class).when(ngpSessionManager).execute(any());
        ngpServiceHandlerSpy.voidTransaction();
    }

    @Test
    public void testVoidTransactionWithPrinter() throws Exception {

        Map<String, Object> mapAdditional = new HashMap<>();
        mapAdditional.put("TPRS_RECEIPT", "          This is Header Line 1         \r\n          This is Header Line 2         \r\n\r\n                                      \r\n------------------------------------------\r\n\\x{BOLD}Omschrijving\\x                        \\x{BOLD}Bedrag\\x\r\n                    \r\nPIZZA BELLISIMA                         2.29\r\n                      ---------------\r\n\\x{DHIGH}Totaal                                2.29\r\n------------------------------------------\r\nEFT Offline       EUR              2.29\r\n   EFT-TA Nr.    = 995\r\n   Card Nr.      = \r\n   Rekeningnummer= 00/unknown\r\n   Rekening nr.  = \r\n   Geldig tot    = 22/09\r\n   \r\n          \r\n     BTW          Netto     Bruto\r\n  ------------------------------        \r\n      9.00          2.10       2.29\r\n          \r\nDatum     Tijd Winkel Kas  Bed   Bonnr.\r\n31.08.20 16:20     37   6          995\r\nThis is Footer Line 1\r\nThis is Footer Line 2\r\n\r\n                                      \r\n\r\n\\x{Bar, PTR_BCS_Code128,500,1000} 660000037000060000099520200831162037\r\n");
        when(ngpSession.getNgpOrderResponse().getAdditional()).thenReturn(mapAdditional);

        when(posService.voidOrder(any(PosService.voidOrderBuilder.class))).thenReturn(order);
        when(propertiesSpy.isPosHandlePrinter()).thenReturn(true);
        when(ngpSession.getNgpOrderResponse()).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.voidTransaction();

        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).voidOrder(any(PosService.voidOrderBuilder.class));
        verify(ngpSession.getNgpOrderResponse()).getId();
        verify(ngpSession.getNgpOrderResponse()).getVersion();
    }

    @Test
    public void testSuspendTransaction() throws Exception {
        when(posService.suspendOrder(any(PosService.suspendOrderBuilder.class))).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.suspendTransaction();

        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).suspendOrder(any(PosService.suspendOrderBuilder.class));
        verify(ngpSession.getNgpOrderResponse()).getId();
        verify(ngpSession.getNgpOrderResponse()).getVersion();
    }

    @Test
    public void testSuspendTransactionWithPrinter() throws Exception {

        Map<String, Object> mapAdditional = new HashMap<>();
        mapAdditional.put("TPRS_RECEIPT", "          This is Header Line 1         \r\n          This is Header Line 2         \r\n\r\n                                      \r\n------------------------------------------\r\n\\x{BOLD}Omschrijving\\x                        \\x{BOLD}Bedrag\\x\r\n                    \r\nPIZZA BELLISIMA                         2.29\r\n                      ---------------\r\n\\x{DHIGH}Totaal                                2.29\r\n------------------------------------------\r\nEFT Offline       EUR              2.29\r\n   EFT-TA Nr.    = 995\r\n   Card Nr.      = \r\n   Rekeningnummer= 00/unknown\r\n   Rekening nr.  = \r\n   Geldig tot    = 22/09\r\n   \r\n          \r\n     BTW          Netto     Bruto\r\n  ------------------------------        \r\n      9.00          2.10       2.29\r\n          \r\nDatum     Tijd Winkel Kas  Bed   Bonnr.\r\n31.08.20 16:20     37   6          995\r\nThis is Footer Line 1\r\nThis is Footer Line 2\r\n\r\n                                      \r\n\r\n\\x{Bar, PTR_BCS_Code128,500,1000} 660000037000060000099520200831162037\r\n");
        when(ngpSession.getNgpOrderResponse().getAdditional()).thenReturn(mapAdditional);

        when(posService.suspendOrder(any(PosService.suspendOrderBuilder.class))).thenReturn(order);
        when(propertiesSpy.isPosHandlePrinter()).thenReturn(true);
        when(ngpSession.getNgpOrderResponse()).thenReturn(order);

        ngpServiceHandlerSpy.initializeWebService();
        ngpServiceHandlerSpy.suspendTransaction();

        verify(ngpSessionManager, times(2)).execute(any());
        verify(posService).suspendOrder(any(PosService.suspendOrderBuilder.class));
        verify(ngpSession.getNgpOrderResponse()).getId();
        verify(ngpSession.getNgpOrderResponse()).getVersion();
    }


    @Test
    public void testSuspendTransactionNotInitialized() throws Exception {
        expectedException.expect(Exception.class);
        ngpServiceHandlerSpy.suspendTransaction();
    }

    @Test
    public void testSuspendTransactionFailed() throws Exception {
        expectedException.expect(ErrorResponseException.class);
        doThrow(ErrorResponseException.class).when(ngpSessionManager).execute(any());
        ngpServiceHandlerSpy.suspendTransaction();
    }

    @Test
    public void testSignOffTpnet() {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");
        when(posService.tpnetSignOff(any(PosService.tpnetSignOffBuilder.class))).thenReturn(null);
        when(eftWrapperClient.close(any(EftContext.class))).thenReturn(TestSupport.getObject(EftResponse.class, "eft-responses/success.json"));

        ngpServiceHandler.signOff();
        verify(posService).tpnetSignOff(any(PosService.tpnetSignOffBuilder.class));
    }

    @Test
    public void testSignOffTpnetException() throws Exception {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "tpnet");
        expectedException.expect(ClientErrorResponseException.class);

        doThrow(ClientErrorResponseException.class).when(posService).tpnetSignOff(any(PosService.tpnetSignOffBuilder.class));

        ngpServiceHandlerSpy.signOff();

    }

    @Test
    public void testSignOffNgp() {
        ReflectionTestUtils.setField(propertiesSpy, "posType", "ngp");
        ngpServiceHandlerSpy.signOff();
        verify(posService, times(0)).tpnetSignOff(any(PosService.tpnetSignOffBuilder.class));
    }

    @Test
    public void testPrintReceiptThrowsForNullReceipt() throws Exception {
        when(ngpSession.getReceipt()).thenReturn(null);
        when(propertiesSpy.isPosHandlePrinter()).thenReturn(true);

        ngpServiceHandler.initializeWebService();
        assertThatThrownBy(() -> ngpServiceHandler.printReceipt())
                .hasMessage("No transaction receipts were available.");
    }

    @Test
    public void testPrintReceiptThrowsForPosHandlePrinterFalse() throws Exception {
        when(ngpSession.getReceipt()).thenReturn(new ReceiptData());
        when(propertiesSpy.isPosHandlePrinter()).thenReturn(false);

        ngpServiceHandler.initializeWebService();
        assertThatThrownBy(() -> ngpServiceHandler.printReceipt())
                .hasMessage("No transaction receipts were available.");
    }

    @Test
    public void testPrintReceipt() throws Exception {
        when(ngpSession.getReceipt()).thenReturn(new ReceiptData());
        when(propertiesSpy.isPosHandlePrinter()).thenReturn(true);

        ngpServiceHandler.initializeWebService();
        ngpServiceHandler.printReceipt();

        verify(ngpSession, times(2)).getReceipt();
        verify(printerHandler).printReceipt(ngpSession.getReceipt());
    }

}