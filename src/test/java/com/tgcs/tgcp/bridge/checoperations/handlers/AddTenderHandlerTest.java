package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.PaymentInfo;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.addtender.AddTenderResponse;
import com.tgcs.tgcp.bridge.checoperations.model.addtender.AddTenderResult;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReceiptSection;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.totals.TotalsEvent;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.exception.ExceptionType;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import com.tgcs.tgcp.bridge.webservice.ngp.WsErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AddTenderHandler.class, ExceptionHandler.class})
@SpringBootTest(classes = {ConfProperties.class})
public class AddTenderHandlerTest {

    @Autowired
    private AddTenderHandler addTenderHandler;

    @MockBean
    private TcpClient tcpClient;

    @SpyBean
    private BridgeSession session;

    @MockBean
    private XmlDocument message;

    @MockBean
    private IWebService webService;

    @MockBean
    private OrderAdapter orderAdapter;

    @Captor
    private ArgumentCaptor argumentCaptor;

    @Spy
    private AddTenderHandler addTenderHandlerSpy;

    @Before
    public void setUp() {
        addTenderHandler.setBridgeSession(session);
        when(session.getOrderAdapter()).thenReturn(orderAdapter);
        when(message.getOrDefaultNodeTextContent(ChecMessagePath.ADD_TENDER_REQUEST_ID, null)).thenReturn("11");
        when(message.getSilentNodeTextContent(ChecMessagePath.DEBIT_IDENTIFIER_TENDER + "/Amount")).thenReturn("5.00");
        when(message.getSilentNodeTextContent(ChecMessagePath.CREDIT_IDENTIFIER_TENDER + "/Amount")).thenReturn("5.00");
        when(message.getSilentNodeTextContent(ChecMessagePath.CASH_IDENTIFIER_TENDER + ChecMessagePath.AMOUNT)).thenReturn("5.00");

        doReturn(new BigDecimal("5")).when(orderAdapter).getTotal();
        doReturn(new BigDecimal("4")).when(orderAdapter).getSubtotal();
        doReturn(new BigDecimal("1")).when(orderAdapter).getTotalTax();
        doReturn(new BigDecimal("1")).when(orderAdapter).getTotalItemCount();
        doReturn(new BigDecimal("5")).when(orderAdapter).getTotalPayments();
        doReturn(new BigDecimal("0")).when(orderAdapter).getPaymentShortage();
        doReturn(new BigDecimal("-0.33")).when(orderAdapter).getCouponTotal();
        doReturn(new BigDecimal("-6.66")).when(orderAdapter).getTotalPriceModifications();
        doReturn("5").when(orderAdapter).getBalanceDue();
        doReturn("0").when(orderAdapter).getChangeDue();

    }

    @Test
    public void processChecMessageTest() throws Exception {
        when((orderAdapter.isOrderFullyPaid())).thenReturn(true);

        doReturn(orderAdapter).when(webService).addPayment(any(PaymentInfo.class));

        addTenderHandler.processChecMessage(tcpClient, message);

        verify(session).setOrderAdapter(orderAdapter);
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addPayment(any(PaymentInfo.class));
        verify(webService).finishTransaction();
        verify(session, times(0)).clearOrder();
    }

    @Test
    public void testAddPaymentThrowsException() throws Exception {
        doThrow(Exception.class).when(webService).addPayment(any(PaymentInfo.class));
        addTenderHandler.processChecMessage(tcpClient, message);

        verify(webService).addPayment(any(PaymentInfo.class));
        verify(session, times(0)).setOrderAdapter(any(OrderAdapter.class));
        verify(webService, times(0)).finishTransaction();
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        AddTenderResponse addTenderResponse = (AddTenderResponse) argumentCaptor.getValue();
        assertNotNull(addTenderResponse.getAddTenderResult().getExceptionResult());
        assertNull(addTenderResponse.getAddTenderResult().getDebitInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCashInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCreditInfo());
    }

    @Test
    public void testTransactionStatusNotSentWhenFinishTransactionFails() throws Exception {
        when((orderAdapter.isOrderFullyPaid())).thenReturn(true);
        doReturn(orderAdapter).when(webService).addPayment(any(PaymentInfo.class));

        addTenderHandler.processChecMessage(tcpClient, message);

        verify(session).setOrderAdapter(orderAdapter);
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addPayment(any(PaymentInfo.class));
        verify(session, times(0)).clearOrder();
    }

    @Test
    public void testProcessChecMessageFullCashPayment() {
        when((orderAdapter.isOrderFullyPaid())).thenReturn(true);
        when(webService.finishTransaction()).thenReturn(true);
        addTenderHandler.processChecMessage(tcpClient, message);
        verify(webService).finishTransaction();
        verify(session).clearOrder();
        verify(tcpClient, times(4)).sendResponseToChecApp(argumentCaptor.capture());
        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEvent = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEvent.getSection());
        assertTrue(posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().get(0).getTextReceiptLine().getText().contains("Cash"));

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("11", totalsEvent.getRequestId());
        assertEquals("5", totalsEvent.getTransactionTotals().getTotal());

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(2);
        assertEquals("11", transactionStatusEvent.getRequestId());

        AddTenderResponse addTenderResponse = (AddTenderResponse) events.get(3);
        assertNotNull(addTenderResponse.getAddTenderResult().getCashInfo());
        assertNull(addTenderResponse.getAddTenderResult().getDebitInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCreditInfo());
    }

    @Test
    public void testProcessChecMessagePartialCashPayment() throws Exception {
        when((orderAdapter.isOrderFullyPaid())).thenReturn(false);
        doReturn(true).when(message).checkPathExists(ChecMessagePath.CASH_IDENTIFIER_TENDER);
        doReturn(new BigDecimal("15")).when(orderAdapter).getTotal();

        addTenderHandler.processChecMessage(tcpClient, message);
        verify(webService, times(0)).finishTransaction();
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());
        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEvent = (POSReceiptEvent) events.get(0);
        assertTrue(posReceiptEvent.getFormattedReceiptLineList().getReceiptLineList().get(0).getTextReceiptLine().getText().contains("Cash"));

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("11", totalsEvent.getRequestId());
        assertEquals("15", totalsEvent.getTransactionTotals().getTotal());
        assertEquals("5", totalsEvent.getTransactionTotals().getBalanceDue());

        AddTenderResponse addTenderResponse = (AddTenderResponse) events.get(2);
        assertNotNull(addTenderResponse.getAddTenderResult().getCashInfo());
        assertNull(addTenderResponse.getAddTenderResult().getDebitInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCreditInfo());
    }

    @Test
    public void testProcessChecMessageDebitPayment() throws Exception {
        doReturn(true).when(message).checkPathExists(ChecMessagePath.DEBIT_IDENTIFIER_TENDER);
        when((orderAdapter.isOrderFullyPaid())).thenReturn(true);
        when(webService.finishTransaction()).thenReturn(true);
        addTenderHandler.processChecMessage(tcpClient, message);
        verify(webService).finishTransaction();
        verify(tcpClient, times(4)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent tsv = (TransactionStatusEvent) events.get(2);
        assertEquals("11", tsv.getRequestId());
        assertEquals(TransactionStatusType.TRANSACTION_END.getValue(), tsv.getTransactionStatus().getStatus());

        AddTenderResponse addTenderResponse = (AddTenderResponse) events.get(3);
        assertNotNull(addTenderResponse.getAddTenderResult().getDebitInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCashInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCreditInfo());

    }

    @Test
    public void testProcessChecMessageCreditPayment() throws Exception {
        doReturn(true).when(message).checkPathExists(ChecMessagePath.CREDIT_IDENTIFIER_TENDER);
        when((orderAdapter.isOrderFullyPaid())).thenReturn(true);
        when(webService.finishTransaction()).thenReturn(true);
        addTenderHandler.processChecMessage(tcpClient, message);
        verify(webService).finishTransaction();
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        TotalsEvent totalsEvent = (TotalsEvent) events.get(0);
        assertEquals("0", totalsEvent.getTransactionTotals().getChangeDue());

        TransactionStatusEvent tsv = (TransactionStatusEvent) events.get(1);
        assertEquals("11", tsv.getRequestId());
        assertEquals(TransactionStatusType.TRANSACTION_END.getValue(), tsv.getTransactionStatus().getStatus());

        AddTenderResponse addTenderResponse = (AddTenderResponse) events.get(2);
        assertNotNull(addTenderResponse.getAddTenderResult().getCreditInfo());
        assertNull(addTenderResponse.getAddTenderResult().getCashInfo());
        assertNull(addTenderResponse.getAddTenderResult().getDebitInfo());

    }

    @Test
    public void testProcessChecMessageRVMCouponPayment() {
        doReturn(true).when(message).checkPathExists(ChecMessagePath.MISC_IDENTIFIER_TENDER);
        doReturn("0.10").when(message).getSilentNodeTextContent(ChecMessagePath.MISC_IDENTIFIER_TENDER + ChecMessagePath.AMOUNT);
        doReturn("RVMCoupon").when(message).getSilentNodeTextContent(ChecMessagePath.MISC_IDENTIFIER_TENDER + ChecMessagePath.DESCRIPTION);
        doReturn(Collections.singletonMap("RVMBarcode", "123456789")).when(message).getKeyValuePairMap(ChecMessagePath.ADD_TENDER_REQUEST_PARAMETER_EXTENSION, ChecMessagePath.KEY_NAME, ChecMessagePath.VALUE_NAME);
        doReturn("123456789").when(message).getSilentNodeTextContent(ChecMessagePath.ADD_TENDER_REQUEST_PARAMETER_EXTENSION + "/" + ChecMessagePath.KEY_VALUE_PAIR_NAME + "/" + ChecMessagePath.VALUE_NAME);
        when((orderAdapter.isOrderFullyPaid())).thenReturn(true);
        when(webService.finishTransaction()).thenReturn(true);

        PaymentInfo rvmCoupon = new PaymentInfo().setAmount("0.50").setPaymentId("123456789").setPaymentType("RVMCoupon").setPaymentTypeGroup(PaymentInfo.PaymentTypeGroup.OTHER);
        PaymentInfo notRvmCoupon = new PaymentInfo().setAmount("1.00").setPaymentId("99").setPaymentType("CASH").setPaymentTypeGroup(PaymentInfo.PaymentTypeGroup.CASH);
        List<PaymentInfo> payments = new ArrayList<>();
        payments.add(rvmCoupon);
        payments.add(notRvmCoupon);

        doReturn(payments).when(orderAdapter).getPaymentInfoList();

        addTenderHandler.processChecMessage(tcpClient, message);
        verify(webService).finishTransaction();
        verify(tcpClient, times(4)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        AddTenderResult addTenderResponse = ((AddTenderResponse) events.get(3)).getAddTenderResult();
        assertEquals("11", addTenderResponse.getRequestId());
        assertNotNull(addTenderResponse.getMiscInfo());
        assertEquals("0.50", addTenderResponse.getMiscInfo().getAmount());
        assertEquals("RVMCoupon", addTenderResponse.getMiscInfo().getDescription());
    }

    @Test
    public void testProcessChecMessagePaymentError() throws Exception {
        WsErrorResponse ex = new WsErrorResponse();
        ex.errorType(ExceptionType.GENERAL.getValue());
        ex.error("TPNET_ADD_PAYMENT_FAILED");
        ex.wsMessage("Error message");

        doThrow(ex).when(webService).addPayment(any());

        addTenderHandler.processChecMessage(tcpClient, message);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddTenderResponse) argumentCaptor.getValue()).getAddTenderResult().getExceptionResult());
        assertEquals("TPNET_ADD_PAYMENT_FAILED", ((AddTenderResponse) argumentCaptor.getValue()).getAddTenderResult().getExceptionResult().getErrorCode());
        assertEquals("Error message", ((AddTenderResponse) argumentCaptor.getValue()).getAddTenderResult().getExceptionResult().getMessage());
    }

}