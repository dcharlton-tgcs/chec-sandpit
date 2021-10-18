package com.tgcs.tgcp.bridge.checoperations.model.status;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:QueryStatusResponse")
@Component
@Scope("prototype")
public class QueryStatusResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "QueryStatusResult")
    private QueryStatusResult queryStatusResult;

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    public QueryStatusResponse setQueryStatusResult(QueryStatusResult queryStatusResult) {
        this.queryStatusResult = queryStatusResult;
        return this;
    }

    public QueryStatusResult getQueryStatusResult() {
        return queryStatusResult;
    }
}
