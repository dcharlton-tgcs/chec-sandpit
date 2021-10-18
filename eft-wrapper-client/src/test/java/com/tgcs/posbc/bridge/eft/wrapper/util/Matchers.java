package com.tgcs.posbc.bridge.eft.wrapper.util;

import com.tgcs.posbc.bridge.eft.wrapper.exception.EftException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.tgcs.posbc.bridge.eft.wrapper.model.BanksysBeError.ServiceWrapper.CLOSED_DEVICE;
import static com.tgcs.posbc.bridge.eft.wrapper.model.BanksysBeError.Transaction.TRANSACTION_REFUSED_BY_TERMINAL;

public class Matchers {

    public static Matcher<EftException> isDeviceClosed() {
        return new BaseMatcher<EftException>()
        {
            @Override
            public void describeTo(Description description) {
                description.appendText("Device closed");
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("Device not closed");
            }

            @Override
            public boolean matches(Object item) {
                if (item == null) { return false; }
                if (item instanceof EftException) {
                    EftException ex = (EftException)item;
                    return ex.getCode() == CLOSED_DEVICE && ex.getMessage().equals("Open not called.");
                }
                return false;
            }
        };
    }

    public static Matcher<EftException> isTransactionRefused() {
        return new BaseMatcher<EftException>()
        {
            @Override
            public void describeTo(Description description) {
                description.appendText("Code 5009");
            }

            @Override
            public boolean matches(Object item) {
                if (item == null) { return false; }
                if (item instanceof EftException) {
                    EftException ex = (EftException)item;
                    return ex.getCode() == TRANSACTION_REFUSED_BY_TERMINAL
                            && ex.getMessage().equals("TRANSACTIE GEWEIGERD\\nDOOR TERMINAL");
                }
                return false;
            }
        };
    }

}
