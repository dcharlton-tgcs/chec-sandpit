package com.tgcs.tgcp.bridge.checoperations.model;

public interface ChecMessagePath {

    String INITIALIZE_REQUEST_ID = "Initialize/InitializeRequest/RequestID";
    String TERMINATE_REQUEST_ID = "Terminate/TerminateRequest/RequestID";
    String TERMINATE_VOID = "Terminate/TerminateRequest/VoidTransaction";
    String QUERY_STATUS_REQUEST_ID = "QueryStatus/QueryStatusRequest/RequestID";
    String REPORT_STATUS_EVENTS_REQUEST_ID = "ReportStatusEvents/ReportStatusEventsRequest/RequestID";
    String PRINT_CURRENT_RECEIPTS_REQUEST_ID = "PrintCurrentReceipts/PrintCurrentReceiptsRequest/RequestID";
    String SUSPEND_TRANSACTION_REQUEST_ID = "Suspend/SuspendTransactionRequest/RequestID";
    String VOID_TRANSACTION_REQUEST_ID = "VoidTransaction/VoidTransactionRequest/RequestID";
    String GET_TOTALS_REQUEST_ID = "GetTotals/GetTotalsRequest/RequestID";
    String SIGN_OFF_REQUEST_ID = "SignOff/SignOffRequest/RequestID";

    String ADD_ITEM_REQUEST_ID = "AddItem/AddItemRequest/RequestID";
    String ADD_ITEM_BARCODE_SCAN_DATA_LABEL = "AddItem/AddItemRequest/ItemIdentifier/BarCode/ScanDataLabel";
    String ADD_ITEM_BARCODE_SCAN_DATA_TYPE = "AddItem/AddItemRequest/ItemIdentifier/BarCode/ScanDataType";
    String ADD_ITEM_VOID_FLAG = "AddItem/AddItemRequest/ItemIdentifier/VoidFlag";

    String ADD_ITEM_KEYED_ITEM_ID = "AddItem/AddItemRequest/ItemIdentifier/KeyedItemID";
    String ADD_ITEM_ID_INCLUDES_CHECK_DIGIT = "AddItem/AddItemRequest/ItemIdentifier/ItemIDIncludesCheckDigit";

    // If the item requires to be weighted, this path will be present in the POSBC message.
    String ADD_ITEM_SCALE_WEIGHT = "AddItem/AddItemRequest/ItemIdentifier/ScaleWeight";
    String ADD_ITEM_QUANTITY = "AddItem/AddItemRequest/ItemIdentifier/Quantity";

    String ADD_TENDER_REQUEST_ID = "AddTender/AddTenderRequest/RequestID";

    String CASH_IDENTIFIER_TENDER = "AddTender/AddTenderRequest/CashIdentifier";
    String DEBIT_IDENTIFIER_TENDER = "AddTender/AddTenderRequest/DebitIdentifier";
    String CREDIT_IDENTIFIER_TENDER = "AddTender/AddTenderRequest/CreditIdentifier";
    String MISC_IDENTIFIER_TENDER = "AddTender/AddTenderRequest/MiscIdentifier";

    String ADD_TENDER_REQUEST_PARAMETER_EXTENSION = "/AddTender/AddTenderRequest/ParameterExtension";
    String KEY_VALUE_PAIR_NAME = "KeyValuePair";
    String KEY_NAME = "Key";
    String VALUE_NAME = "Value";

    String AMOUNT = "/Amount";
    String DESCRIPTION = "/Description";
    String IS_VOID = "/IsVoid";

    String ADD_COUPON_REQUEST_ID = "AddCoupon/AddCouponRequest/RequestID";
    String ADD_COUPON_BARCODE_SCAN_DATA_LABEL = "AddCoupon/AddCouponRequest/CouponIdentifier/BarCode/ScanDataLabel";
    String ADD_COUPON_CODE = "AddCoupon/AddCouponRequest/CouponIdentifier/Code";
    String ADD_COUPON_AMOUNT = "AddCoupon/AddCouponRequest/CouponIdentifier/Amount";

    String ADD_CUSTOMER_REQUEST_ID = "AddCustomer/AddCustomerRequest/RequestID";
    String ADD_CUSTOMER_CUSTOMER_ID = "AddCustomer/AddCustomerRequest/CustomerIdentifier/CustomerID";
    String ADD_CUSTOMER_TYPE = "AddCustomer/AddCustomerRequest/CustomerIdentifier/Type";
    String ADD_CUSTOMER_EXPIRATION_DATE = "AddCustomer/AddCustomerRequest/CustomerIdentifier/ExpirationDate/Date";

    String ITEM_ENTRY_METHOD_KEYED = "KeyedItemCode";

    String ADD_CUSTOMER_BIRTHDATE_REQUEST_ID = "AddCustomerBirthdate/AddCustomerBirthdateRequest/RequestID";

    String ADD_RECEIPT_LINES_REQUEST_ID = "AddReceiptLines/AddReceiptLinesRequest/RequestID";

    String REPRINT_RECEIPTS_REQUEST_ID = "ReprintReceipts/ReprintReceiptsRequest/RequestID";

    String UNKNOWN_REQUEST_ID = "{rootNodeName}/{rootNodeName}Request/RequestID";
}
