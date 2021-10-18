package com.tgcs.tgcp.bridge.webservice.ngp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgcs.tgcp.bridge.common.utils.StringFormatterCustom;
import com.tgcs.tgcp.framework.core.error.ClientErrorResponseException;
import com.tgcs.tgcp.framework.model.*;
import com.tgcs.tgcp.framework.spring.client.ErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WsErrorResponse extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(WsErrorResponse.class);

    String errorType;
    String errorLevel;
    String error;
    String wsMessage;
    int statusCode;

    public WsErrorResponse() {

    }

    public WsErrorResponse(Throwable t) {
        super(t);
    }

    public WsErrorResponse errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public WsErrorResponse errorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
        return this;
    }

    public WsErrorResponse error(String error) {
        this.error = error;
        return this;
    }

    public WsErrorResponse wsMessage(String message) {
        this.wsMessage = message;
        return this;
    }

    public WsErrorResponse statusCode(int code) {
        this.statusCode = code;
        return this;
    }

    public String getError() {
        return error;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public String getErrorType() {
        return errorType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getWsMessage() {
        return wsMessage;
    }

    public static Exception decodeNgpException(ErrorResponseException rse) {
        Map<String, Object> map = null;
        WsErrorResponse errorResponse = new WsErrorResponse(rse);
        try {
            errorResponse.statusCode(rse.getStatusCode())
                    .wsMessage(StringFormatterCustom.getJsonValueForKey(rse.getMessage(), "message"));

            map = new ObjectMapper().readValue(rse.getMessage(), Map.class);
            Object obj = map.get("errors");
            if (obj instanceof ArrayList) {
                ArrayList errorList = (ArrayList) obj;
                if (errorList.size() > 0 && errorList.get(0) instanceof LinkedHashMap) {
                    LinkedHashMap errorMap = (LinkedHashMap) errorList.get(0);
                    errorResponse.errorLevel((String) errorMap.get("errorLevel"))
                            .errorType((String) errorMap.get("errorType"))
                            .error((String) errorMap.get("error"));
                }
            }
        } catch (IOException e) {
            logger.error("Error decoding exception", e);
        }
        return errorResponse;
    }

    public static Exception decodeNgpException(ClientErrorResponseException rse) {
        WsErrorResponse errorResponse = new WsErrorResponse(rse);

        errorResponse.wsMessage(replaceParmsFromErrorMessage(rse));

        try {
            Object returnObject = rse.getReturnObject();
            if (returnObject != null) {
                // we're only interested in the first error
                if (returnObject instanceof ClientErrorGeneric) {
                    ClientErrorEntryGeneric errorEntry = ((ClientErrorGeneric) returnObject).getErrors().get(0);
                    errorResponse.error(errorEntry.getError())
                            .errorType(errorEntry.getErrorType())
                            .errorLevel(errorEntry.getErrorLevel().value);
                } if (returnObject instanceof ClientErrorValidation) {
                    ClientErrorValidationErrorEntry errorEntry = ((ClientErrorValidation) returnObject).getErrors().get(0);
                    ClientErrorValidationErrorEntryDetails details = errorEntry.getDetails();
                    String errorMessage = null;
                    if(details != null) {
                        Map<String, Object> factsMap = details.getFacts();
                        Map<String, String> facts = factsMap.entrySet().stream()
                                                            .filter(e -> e.getValue() instanceof String)
                                                            .collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
                        errorMessage = StringFormatterCustom.replaceParams(details.getPrompt().get("default"), facts);
                    }

                    errorResponse.error(errorEntry.getError())
                            .errorType(errorEntry.getErrorType())
                            .errorLevel(errorEntry.getErrorLevel().value);
                    if(errorMessage != null){
                        errorResponse.wsMessage(errorMessage);
                    }
                } else if (returnObject instanceof ClientErrorMissingField) {
                    ClientErrorMissingFieldErrorEntry errorEntry = ((ClientErrorMissingField) returnObject).getErrors().get(0);
                    errorResponse.error(errorEntry.getError())
                            .errorType(errorEntry.getErrorType())
                            .errorLevel(errorEntry.getErrorLevel().value);
                } // TODO check for more ClientErrorTypes
            }
        } catch (Exception e) {
            logger.error("Error decoding exception", e);
        }
        return errorResponse;
    }

    private static String replaceParmsFromErrorMessage(ClientErrorResponseException rse) {
        try {
            if (rse.getMessageVariables() != null) {
                Map<String, String> messageVariables = rse.getMessageVariables().entrySet().stream()
                        .filter(e -> e.getValue() != null)
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

                return StringFormatterCustom.replaceParams(rse.getMessage(), messageVariables);
            }
        } catch (Exception e) {
            logger.error("Error replace params from error message", e);
        }
        return rse.getMessage();
    }
}
