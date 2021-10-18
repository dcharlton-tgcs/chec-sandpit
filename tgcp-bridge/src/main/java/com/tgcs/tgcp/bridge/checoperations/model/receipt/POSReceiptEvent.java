package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:POSReceiptEvent")
public class POSReceiptEvent implements ResponseType {

    public enum UpdateType {
        Add,
        Modify,
        Remove,
        EndReceipt;
    };

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace ="http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlAttribute(name = "UpdateType")
    protected String updateType = "Add";

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "Type")
    private String type = "Customer";

    @XmlElement(name = "Index")
    private String index = "0";

    @XmlElement(name = "Section")
    private String section;

    @XmlElement(name = "Group")
    private String group;

    @XmlElement(name = "FormattedReceiptLineList")
    private FormattedReceiptLineList formattedReceiptLineList;

    public POSReceiptEvent() {
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.EVENT.getValue();
    }

    public String getUpdateType() {
        return updateType;
    }

    public POSReceiptEvent setUpdateType(String updateType) {
        this.updateType = updateType;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public POSReceiptEvent setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getType() {
        return type;
    }

    public POSReceiptEvent setType(String type) {
        this.type = type;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public POSReceiptEvent setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getSection() {
        return section;
    }

    public POSReceiptEvent setSection(String section) {
        this.section = section;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public POSReceiptEvent setGroup(String group) {
        this.group = group;
        return this;
    }

    public FormattedReceiptLineList getFormattedReceiptLineList() {
        return formattedReceiptLineList;
    }

    public POSReceiptEvent setFormattedReceiptLineList(FormattedReceiptLineList formattedReceiptLineList) {
        this.formattedReceiptLineList = formattedReceiptLineList;
        return this;
    }
}
