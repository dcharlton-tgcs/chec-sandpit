package com.tgcs.tgcp.bridge.exception;

public interface WebServiceException {

    String CONNECTION_REFUSED = "CONNECTION_REFUSED";
    String TIMEOUT = "TIMEOUT";
    String FORBIDDEN = "FORBIDDEN";
    String UNAUTHORIZED = "UNAUTHORIZED";

    String CONNECTION_REFUSED_MSG = "Connection refused";
    String TIMEOUT_MSG = "Time out connecting to WebService";
    String FORBIDDEN_MSG = "403 Forbidden";
    String UNAUTHORIZED_MSG = "400 Unauthorized";

}
