package com.tgcs.tgcp.bridge.checoperations.model.receipt;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FormattedReceiptLineList")
public class FormattedReceiptLineList {

    @XmlElement(name = "FormattedReceiptLine")
    private List<FormattedReceiptLine> formattedReceiptLineList = new ArrayList<>();

    public FormattedReceiptLineList() {

    }

    public FormattedReceiptLineList addReceiptLine(FormattedReceiptLine formattedReceiptLine) {
        this.formattedReceiptLineList.add(formattedReceiptLine);
        return this;
    }

    // Add a receipt line to the list. It needs category and type (HEADER or ITEM) and a message string.
    public FormattedReceiptLineList addReceiptLine(LineCategory category, LineType type, String message) {
        this.formattedReceiptLineList.add(new FormattedReceiptLine(category, type, message));
        return this;
    }

    // Function to create the static receipt header. It will use the String[] messages defined below. It will pass HEADER as default category
    // and type for the CreateReceiptLine method
    // Maybe put in the properties? Static external file?
    public FormattedReceiptLineList addHeaderLines() {
        for (int i = 0; i < messages.length; i++) {
            addReceiptLine(LineCategory.HEADER, LineType.HEADER, messages[i]);
        }
        return this;
    }

    public List<FormattedReceiptLine> getReceiptLineList() {
        return formattedReceiptLineList;
    }

    // Create Header Messages
    static String[] messages = new String[4];

    {
        messages[0] = "***************************************";
        messages[1] = "  Welcome to Self-Checkout Automation  ";
        messages[2] = "        Powered by EDC Bridge©         ";
        messages[3] = "***************************************";
    }


}
