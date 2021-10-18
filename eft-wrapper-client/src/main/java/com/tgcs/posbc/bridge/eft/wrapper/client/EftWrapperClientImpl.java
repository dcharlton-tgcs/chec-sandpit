package com.tgcs.posbc.bridge.eft.wrapper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgcs.posbc.bridge.eft.wrapper.client.dto.DebitRequestBody;
import com.tgcs.posbc.bridge.eft.wrapper.client.dto.PaymentTransaction;
import com.tgcs.posbc.bridge.eft.wrapper.client.dto.RequestBody;
import com.tgcs.posbc.bridge.eft.wrapper.config.EftWrapperProperties;
import com.tgcs.posbc.bridge.eft.wrapper.exception.*;
import com.tgcs.posbc.bridge.eft.wrapper.model.BanksysBeError;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftResponse;
import com.tgcs.posbc.bridge.eft.wrapper.util.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class EftWrapperClientImpl implements EftWrapperClient {

    private static final Logger log = LoggerFactory.getLogger(EftWrapperClientImpl.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final EftWrapperProperties properties;

    public EftWrapperClientImpl(RestTemplate restTemplate, EftWrapperProperties properties) {
        this.objectMapper = new ObjectMapper();
        this.properties = properties;

        this.restTemplate = restTemplate;
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(properties.getConnectTimeout() * 1000);
        clientHttpRequestFactory.setReadTimeout(properties.getReadTimeout() * 1000);
        this.restTemplate.setRequestFactory(clientHttpRequestFactory);
        this.restTemplate.setErrorHandler(new EftClientErrorHandler());
        if (LoggerFactory.getLogger(EftWrapperClient.class).isTraceEnabled()) {
            this.restTemplate.setInterceptors(Collections.singletonList(new EftClientLogInterceptor()));
        }
    }

    @Override
    public EftResponse open() throws EftException {
        return request("open", null);
    }

    @Override
    public EftResponse open(EftContext context) throws EftException {
        return request("open", new RequestBody(context));
    }

    @Override
    public EftResponse close() throws EftException {
        return request("close", null);
    }

    @Override
    public EftResponse close(EftContext context) throws EftException {
        return request("close", new RequestBody(context));
    }

    @Override
    public EftResponse reprint(EftContext context) throws EftException {
        return request("reprint", new RequestBody(context));
    }

    @Override
    public EftResponse debitPayment(EftContext context, String requestedAmount) throws EftException {
        Integer formattedAmount = Safe.formatTPNetAmount(requestedAmount);

        DebitRequestBody request = new DebitRequestBody(context)
                .setPaymentTransaction(new PaymentTransaction().value(formattedAmount));

        return request("debit", request);
    }

    /**
     * Handle the request to Rest Service
     *
     * @param path    The last segment path after the context path. Should remove the slash.
     * @param payload The request body to be send. Must extends RequestBody class.
     * @return the EftResponse ! Thanks Captain Obvious.
     * @throws EftException : Error from EFT service (original code and message)
     *         EftTimeoutException : EFT service wrapper read timeout. See application.properties for fine tuning.
     *         EftRequestException : EFT service wrapper request error.
     *         ReprintNeededException : when previous transaction was not complete
     */
    private <T extends RequestBody> EftResponse request(String path, T payload) throws EftException {
        try {
            String url = properties.urlBuilder().pathSegment(path).build().toUriString();
            log.info(String.format("Eft call : %s", url));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonString = this.restTemplate
                    .postForObject(url, new HttpEntity<>(payload, headers), String.class);

            if (jsonString == null) throw new EftRequestException("EFt Service response message error");
            log.trace(Safe.singleLineJsonString(jsonString));

            return handleEftResponse(this.objectMapper.readValue(jsonString, EftResponse.class));

        } catch (EftException e) {
            log.error("Eft service call error", e);
            throw e;
        } catch (Exception e) {
            log.error("Eft service call error", e);
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new EftTimeoutException();
            } else {
                throw new EftRequestException(e);
            }
        }
    }

    private static EftResponse handleEftResponse(EftResponse response) throws EftException {
        int code = response.getResultCode();

        if (BanksysBeError.hasNoError(code)) {
            return response;

        } else if (
            code == BanksysBeError.Transaction.PAID_PARTIALLY ||
            code == BanksysBeError.ServiceWrapper.REPRINT_NEEDED
        ) {
            throw new ReprintNeededException();

        } else {
            throw new EftException(code, response.getDspText());
        }
    }

    static class EftClientLogInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public @NonNull
        ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, ClientHttpRequestExecution execution) throws IOException {
            log.info(new String(body, StandardCharsets.UTF_8));
            return execution.execute(request, body);
        }
    }

    static class EftClientErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().series() == SERVER_ERROR;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            throw new EftRequestException(response.getStatusText());
        }

        @Override
        public void handleError(@NonNull URI url, @NonNull HttpMethod method, @NonNull ClientHttpResponse response) throws IOException {
            throw new EftRequestException(response.getStatusText());
        }
    }
}
