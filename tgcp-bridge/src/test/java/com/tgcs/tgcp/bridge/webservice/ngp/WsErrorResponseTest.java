package com.tgcs.tgcp.bridge.webservice.ngp;

import com.tgcs.tgcp.framework.core.error.ClientErrorBuilder;
import com.tgcs.tgcp.framework.core.error.ClientErrorResponseException;
import com.tgcs.tgcp.framework.model.ClientErrorLevel;
import com.tgcs.tgcp.framework.model.ClientErrorValidation;
import com.tgcs.tgcp.framework.model.ClientErrorValidationErrorEntry;
import com.tgcs.tgcp.framework.model.ClientErrorValidationErrorEntryDetails;
import com.tgcs.tgcp.pos.tpnet.service.validation.ValidationError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WsErrorResponseTest {

    @Test
    public void testValidationErrorResponseDecoding() {
        WsErrorResponse exception = (WsErrorResponse) WsErrorResponse.decodeNgpException(createValidationError());
        assertEquals("NOT_FOR_SALE", exception.getError());
        assertEquals("VALIDATION_ERROR", exception.getErrorType());
        assertEquals("Warning", exception.getErrorLevel());
        assertEquals("Item 1234 is not for sale.", exception.getWsMessage());
    }

    private ClientErrorResponseException createValidationError() {
        ClientErrorValidation error = new ClientErrorValidation();
        ClientErrorValidationErrorEntry errorEntry = new ClientErrorValidationErrorEntry();
        errorEntry.setError("NOT_FOR_SALE");
        errorEntry.setErrorType(ValidationError.getErrorType());
        errorEntry.setErrorLevel(ClientErrorLevel.WARNING);
        ClientErrorValidationErrorEntryDetails details = new ClientErrorValidationErrorEntryDetails();
        details.putPromptItem("default", "Item {skuId} is not for sale.");
        details.putFactsItem("skuId", "1234");
        errorEntry.setDetails(details);
        error.addErrorsItem(errorEntry);
        return new ClientErrorBuilder("NOT_FOR_SALE", "", "pos", error).createException();
    }
}
