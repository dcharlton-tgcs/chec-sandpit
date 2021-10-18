package com.tgcs.tgcp.bridge.exception;

import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.webservice.ngp.WsErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler implements WebServiceException {

    /**
     * This method will return a ChecOperationException object
     *
     * @param ex = the exception returned by the webservice
     * @return
     */
    public ChecOperationException retrieveWebServiceError(Exception ex) {

        if (ex instanceof WsErrorResponse) {
            return getChecException((WsErrorResponse) ex);
        } else
            return new ChecOperationException(ex.getLocalizedMessage(), ex.getMessage(), ex);
    }

    /**
     * This method will set up the errorCode and errorMessage needed for the ChecOperationException that
     * will be returned to CHEC
     *
     * @param ex = the exception returned by the webservice
     */
    private ChecOperationException getChecException(WsErrorResponse ex) {

        // Exception returned can also be a Gateway timeout, in this case what should we do? Item not found, asking the user to re-scan the item?
        switch (ex.getError()) {
            case CONNECTION_REFUSED:
                return new ChecOperationException(CONNECTION_REFUSED, CONNECTION_REFUSED_MSG, ex);
            case TIMEOUT:
                return new ChecOperationException(TIMEOUT, TIMEOUT_MSG, ex);
            case FORBIDDEN:
                return new ChecOperationException(FORBIDDEN, FORBIDDEN_MSG, ex);
        }

        ChecOperationException checOperEx;

        // Is it an AddItem error?
        checOperEx = checkForAddItemErrors(ex);
        if (checOperEx != null) return checOperEx;

        // Is it and AddTender error?
        checOperEx = checkForAddTenderErrors(ex);
        if (checOperEx != null) return checOperEx;

        // Is it an AddCustomer error?
        checOperEx = checkForAddCustomerErrors(ex);
        if (checOperEx != null) return checOperEx;

        // Is it an AddCoupon error?
        checOperEx = checkForAddCouponErrors(ex);
        if (checOperEx != null) return checOperEx;

        return new ChecOperationException(ex.getLocalizedMessage(), ex.getMessage(), ex);

    }

    private ChecOperationException checkForAddItemErrors(WsErrorResponse ex) {
        ErrorCode addItemErrorCode = getAddItemErrorCode(ex);
        if(addItemErrorCode != null) {
            return new ChecOperationException(addItemErrorCode.getValue(), ex.getWsMessage(), ex);
        }
        return null;
    }

    private ErrorCode getAddItemErrorCode(WsErrorResponse ex) {
        // todo maybe we will need to rework this: we could send a key, and CHEC will use a dictionary to grab the translation for that key
        String errorType = ex.getErrorType();
        String error = ex.getError();
        if (errorType.equals(ExceptionType.NGP_REQUIRED_ATTRIBUTE.getValue())) { // required attribute errors
            if (error.equals(ExceptionDescription.NGP_QUANTITY_NEEDED_UOM_NOT_EACH.getValue())){
                return ErrorCode.ITEM_WEIGHT_REQUIRED;
            }
        } else if (errorType.equals(ExceptionType.NGP_VALIDATION_ERROR.getValue())) { // validation errors
            if (error.equals(ExceptionDescription.QUANTITY_REQUIRED.getValue())) {
                return ErrorCode.ITEM_QUANTITY_REQUIRED;
            } else if (error.equals(ExceptionDescription.NOT_FOR_SALE.getValue())) {
                return ErrorCode.ITEM_NOT_FOR_SALE;
            }
        } else if (errorType.equals(ExceptionType.GENERAL.getValue())) { // general errors
            if (error.equals(ExceptionDescription.BARCODE_NOT_FOUND.getValue())) {
                return ErrorCode.ITEM_NOT_FOUND;
            }
        }
        return null; // not an add item error
    }

    private static ChecOperationException checkForAddTenderErrors(WsErrorResponse ex) {
        String errorCode, errorMessage;

        String orderError = ex.getError();

        if (orderError.equals(ExceptionDescription.TENDER_NOT_SUPPORTED.getValue())
        || orderError.equals(ExceptionDescription.ADD_PAYMENT_FAILED.getValue())) {
            errorCode = ex.getError();
            errorMessage = StringUtils.isBlank(ex.getWsMessage()) ? "Failed to add Tender" : ex.getWsMessage();
            return new ChecOperationException(errorCode, errorMessage, ex);
        }
        return null; // not an add tender error
    }

    private static ChecOperationException checkForAddCustomerErrors(WsErrorResponse ex) {
        String errorCode, errorMessage;

        String orderError = ex.getError();

        if (orderError.equals(ExceptionDescription.CUSTOMER_ALREADY_EXISTS.getValue())) {
            errorCode = ExceptionDescription.CUSTOMER_ALREADY_EXISTS.getValue();
            // todo message is hardcoded now
            errorMessage = "Customer already present in the transaction. Please contact Shopper Assistant";

            return new ChecOperationException(errorCode, errorMessage, ex);
        } else if (orderError.equals(ExceptionDescription.LOYALTY_CARD_NOT_ALLOWED.getValue())) {
            return new ChecOperationException(ErrorCode.LOYALTY_AFTER_ITEM.name(),
                    ErrorCode.LOYALTY_AFTER_ITEM.getValue(), null);
        } else
            return null;
    }

    private static ChecOperationException checkForAddCouponErrors(WsErrorResponse ex) {
        String errorCode;
        String errorMessage = "";

        String orderError = ex.getError();

        if (orderError.equals(ExceptionDescription.COUPON_NOT_FOUND.getValue())) {
            errorCode = ErrorCode.ITEM_NOT_FOUND.getValue();
            //           String offerId = new JSONObject(ex.getResponseBodyAsString()).getJSONObject("messageVariables").getString("OFFER_ID");
            // todo message is hardcoded now
            //           errorMessage = new JSONObject(ex.getResponseBodyAsString()).getString("message").replace("{OFFER_ID}", offerId);

            return new ChecOperationException(errorCode, errorMessage, ex);
        } else
            return null;
    }


}
