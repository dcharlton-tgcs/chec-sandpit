package com.tgcs.tgcp.bridge.webservice.ngp;

import java.util.UUID;

public class NgpRequestIdProvider {

    public static String getUniqRequestId(){
        return UUID.randomUUID().toString();
    }


}
