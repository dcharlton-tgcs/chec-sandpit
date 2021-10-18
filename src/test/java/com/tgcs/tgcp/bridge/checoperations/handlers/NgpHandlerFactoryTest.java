package com.tgcs.tgcp.bridge.checoperations.handlers;


import com.tgcs.tgcp.bridge.checoperations.model.status.QueryStatusResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.util.TestSupport;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {NgpHandlerFactory.class, InitializationHandler.class,
        ExceptionHandler.class, QueryStatusHandler.class, QueryStatusResponse.class, ReportStatusHandler.class,
        AddItemHandler.class, SignOffHandler.class, TerminateHandler.class, SuspendTransactionHandler.class,
        VoidTransactionHandler.class, PrintCurrentReceiptsHandler.class, GetTotalsHandler.class, AddTenderHandler.class,
        AddCouponHandler.class, AddCustomerHandler.class, AddCustomerBirthdateHandler.class, AddReceiptLinesHandler.class,
        ReprintReceiptsHandler.class, UnsupportedOperationHandler.class, XmlDocument.class})
@SpringBootTest(classes = {ConfProperties.class})
public class NgpHandlerFactoryTest {

    @Autowired
    NgpHandlerFactory ngpHandlerFactory;

    @Autowired
    XmlDocument xmlMessage;

    @MockBean
    IWebService webService;

    @Test
    public void testGetNullHandler() {
        assertNull(ngpHandlerFactory.getHandler(null));
    }

    @Test
    public void testGetInitializationHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("init/initializeRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof InitializationHandler);
    }

    @Test
    public void testGetQueryStatusHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("status/queryStatusRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof QueryStatusHandler);
    }

    @Test
    public void testGetReportStatusHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("status/reportStatusEventsRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof ReportStatusHandler);
    }

    @Test
    public void testGetAddItemHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("addItem/addItemRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof AddItemHandler);
    }

    @Test
    public void testGetSignOffHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("close/signOffRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof SignOffHandler);
    }

    @Test
    public void testGetTerminateHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("close/terminateRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof TerminateHandler);
    }

    @Test
    public void testGetSuspendTransactionHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("suspendTransaction/suspendTransactionRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof SuspendTransactionHandler);
    }

    @Test
    public void testGetVoidTransactionHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("voidTransaction/voidTransactionRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof VoidTransactionHandler);
    }

    @Test
    public void testGetPrintCurrentReceiptsHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("receipt/printCurrentReceiptsRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof PrintCurrentReceiptsHandler);
    }

    @Test
    public void testGetTotalsHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("totals/getTotalsRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof GetTotalsHandler);
    }

    @Test
    public void testGetAddTenderHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("addTender/addTenderRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof AddTenderHandler);
    }

    @Test
    public void testGetAddCouponHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("addCoupon/addCouponRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof AddCouponHandler);
    }

    @Test
    public void testGetAddCustomerHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("addCustomer/addCustomerRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof AddCustomerHandler);
    }

    @Test
    public void testGetAddCustomerBirthdateHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("addCustomer/addCustomerBirthdateRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof AddCustomerBirthdateHandler);
    }

    @Test
    public void testGetAddReceiptLinesHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("receipt/addReceiptLinesRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof AddReceiptLinesHandler);
    }

    @Test
    public void testReprintReceiptsHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("receipt/reprintReceiptsRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof ReprintReceiptsHandler);
    }

    @Test
    public void testUnsupportedOperationHandler() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("unsupportedOperation/unsupportedOperationRequest.xml"));
        assertTrue(ngpHandlerFactory.getHandler(xmlMessage) instanceof UnsupportedOperationHandler);
    }

}