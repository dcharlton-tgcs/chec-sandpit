package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.adapter.PaymentInfo;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.*;

import java.math.BigDecimal;

public class AddPaymentRequestBuilder {

    public static AddPaymentRequest build(PaymentInfo paymentInfo, ConfProperties properties, String currencyCode) {

        PaymentEntryData paymentEntryData = new PaymentEntryData();
        paymentEntryData.setType(PaymentInfo.PaymentType.CASH.value.equals(paymentInfo.getPaymentType()) ?
                String.join("_", paymentInfo.getPaymentType(), currencyCode) : paymentInfo.getPaymentType());
        paymentEntryData.setGroup(PaymentTypeGroup.fromValue(paymentInfo.getPaymentTypeGroup().value));

        if(paymentInfo.getAdditional() != null) {
            paymentEntryData.getAdditional().putAll(paymentInfo.getAdditional());
        }

        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setValue(new BigDecimal(paymentInfo.getAmount()));
        currencyValue.setCurrencyCode(CurrencyCode.fromValue(currencyCode));

        ConvertedCurrencyValue convertedCurrencyValue = new ConvertedCurrencyValue();
        convertedCurrencyValue.setValue(new BigDecimal(paymentInfo.getAmount()));
        convertedCurrencyValue.setCurrencyCode(CurrencyCode.fromValue(currencyCode));
        convertedCurrencyValue.setCurrencyValue(currencyValue);

        PaymentTransactionRequestData paymentTransactionRequestData = new PaymentTransactionRequestData();
        paymentTransactionRequestData.setRequestedAmount(convertedCurrencyValue);

        if (PaymentTypeGroup.DEBIT.equals(paymentEntryData.getGroup())) {
            if (!properties.getPosHandleEft()) {
                paymentEntryData.setAccountNumber("MjIyMjIyMDAwMDAwMDAwMA==");
                paymentEntryData.setPinData("0F374D32DE0E699AFFFF3A000104E6210B14");
                paymentEntryData.setDisplayAccountNumber("0000");
                paymentEntryData.setExpirationDate("0923");
                paymentEntryData.setEntryMethod(PaymentEntryMethod.fromValue("KEYED"));
                CustomerName customerName = new CustomerName();
                customerName.setFirstName("");
                customerName.setLastName("");
                paymentEntryData.setCustomerName(customerName);
            }
        }

        AddPaymentRequest addPaymentRequest = new AddPaymentRequest();
        addPaymentRequest.setContext(ContextBuilder.build(properties));
        addPaymentRequest.setPaymentEntryData(paymentEntryData);
        addPaymentRequest.setPaymentTransaction(paymentTransactionRequestData);

        return addPaymentRequest;
    }
}