package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;

public class Validation {

    String id;
    /**
     * objectId (optional)
     */
    String objectId;
    /**
     * objectType (optional)
     */
    String objectType;
    /**
     * objectValue (optional)
     */
    BigDecimal objectValue;
    boolean deferred;
    boolean approvable;
}
