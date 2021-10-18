package com.tgcs.posbc.bridge.eft.wrapper.client;

import com.tgcs.posbc.bridge.eft.wrapper.exception.EftException;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftResponse;

public interface EftWrapperClient {

    /**
     * Open the EFT Device (one a day)
     * @return Eft response Object
     * @throws EftException : original code and message from Eft Service
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete.
     */
    EftResponse open() throws EftException;

    /**     *
     * Open the EFT Device (one a day)
     * @return Eft response Object
     * @throws EftException : original code and message from Eft Service
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete
     */
    EftResponse open(EftContext context) throws EftException;

    /**
     * Close the EFT Device (one a day)
     * @return Eft response Object
     * @throws EftException : original code and message from Eft Service
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete
     */
    EftResponse close() throws EftException;
    /**
     * Close the EFT Device (one a day)
     * @return Eft response Object
     * @throws EftException : original code and message from Eft Service
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete
     */
    EftResponse close(EftContext context) throws EftException;

    /**
     * Reprint the last payment request
     * @return Eft response Object
     * @throws EftException : original code and message from Eft Service
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete
     */
    EftResponse reprint(EftContext context) throws EftException;

    /**
     *
     * @param context the context for any EFT request
     * @param requestedAmount the amount to be payed
     * @return Eft response Object
     * @throws EftException : original code and message from Eft Service
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete
     */
    EftResponse debitPayment(EftContext context, String requestedAmount) throws EftException;
}
