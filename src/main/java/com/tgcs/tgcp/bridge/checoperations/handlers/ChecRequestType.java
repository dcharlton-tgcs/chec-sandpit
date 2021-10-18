package com.tgcs.tgcp.bridge.checoperations.handlers;

public interface ChecRequestType {
    public final static String INITIALIZATION = "Initialize";
    public final static String QUERY_STATUS = "QueryStatus";
    public final static String REPORT_STATUS = "ReportStatusEvents";
    public final static String ADD_ITEM = "AddItem";
    public final static String SIGN_OFF = "SignOff";
    public final static String TERMINATE = "Terminate";
    public final static String VOID_TRANSACTION = "VoidTransaction";
    public final static String SUSPEND_TRANSACTION = "Suspend";
    public final static String PRINT_CURRENT_RECEIPTS = "PrintCurrentReceipts";
    public final static String GET_TOTALS = "GetTotals";
    public final static String ADD_TENDER = "AddTender";
    public final static String ADD_COUPON = "AddCoupon";
    public final static String ADD_CUSTOMER = "AddCustomer";
    public final static String ADD_CUSTOMER_BIRTHDATE = "AddCustomerBirthdate";
    public final static String ADD_RECEIPT_LINES = "AddReceiptLines";
    public final static String REPRINT_RECEIPTS = "ReprintReceipts";
}
