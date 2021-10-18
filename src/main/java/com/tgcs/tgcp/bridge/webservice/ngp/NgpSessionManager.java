package com.tgcs.tgcp.bridge.webservice.ngp;

import com.tgcs.tgcp.authorization.api.service.AuthorizationServiceClient;
import com.tgcs.tgcp.authorization.model.LoginRequest;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.VoidItemException;
import com.tgcs.tgcp.bridge.exception.WebServiceException;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder.ContextBuilder;
import com.tgcs.tgcp.framework.core.error.ClientErrorResponseException;
import com.tgcs.tgcp.framework.spring.client.ErrorResponseException;
import com.tgcs.tgcp.framework.spring.client.SessionTokensUtil;
import com.tgcs.tgcp.framework.spring.client.SpringRestfulAPIHeaders;
import com.tgcs.tgcp.pos.model.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.tgcs.tgcp.authorization.api.service.AuthorizationService.authorizationLoginBuilder;

@Component
@Scope("singleton")
public class NgpSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(NgpSessionManager.class);

    private static final Pattern BEARER_PATTERN = Pattern.compile("^bearer (.+)$", Pattern.CASE_INSENSITIVE);

    @Autowired
    ConfProperties properties;

    @Autowired
    @Qualifier(SpringRestfulAPIHeaders.BEAN_NAME)
    private SpringRestfulAPIHeaders headers;

    @Autowired
    @Qualifier("SessionTokensUtil")
    private SessionTokensUtil sessionTokensUtil;

    @Autowired
    @Qualifier(AuthorizationServiceClient.BEAN_NAME)
    private AuthorizationServiceClient authorizationService;

    public void initAuthorization() throws Exception {
        execute(this::authenticate);
    }

    public interface NgpRequestHandler<T> {
        T statement();
    }

    public <T> T execute(NgpRequestHandler<T> requestHandler) throws Exception {
        assert requestHandler != null;
        return executeRequest(ContextBuilder.build(properties), requestHandler, 1);
    }

    private <T> T executeRequest(Context ctx, NgpRequestHandler<T> requestHandler, int retry) throws Exception {
        while (retry > 0) {
            try {
                if (!this.headers.getGlobalRequestHeaders().containsKey("authorization")) authenticate();

                return Optional.ofNullable(requestHandler.statement()).get();
            } catch (ErrorResponseException rse) {
                if (rse.getStatusCode() == 401 && retry > 0) {
                    logger.debug("Request execution refused. trying re-authenticate, retrying...");
                    authenticate();
                    --retry;
                    continue;
                }
                throw WsErrorResponse.decodeNgpException(rse);
            } catch (ClientErrorResponseException ex) {
                throw WsErrorResponse.decodeNgpException(ex);
            }catch (Exception ex){
                if(ex.getMessage().contains("Connection refused")){
                    throw new WsErrorResponse(ex).error(WebServiceException.CONNECTION_REFUSED);
                }
                throw ex;
            }

        }
        throw new Exception("this is never reached");
    }

    private boolean authenticate() {
        //authorization not used for tpnet, call a library not a service
        if(properties.getPosType().contains("tpnet")) return true;

        authorizationService.authorizationLogin(authorizationLoginBuilder
                .create(new LoginRequest().username("1").password("1")));
        extractJsonWebTokenFromHttpHeaders(this.headers.getResponseHeaders());
        return true;
    }


    private void extractJsonWebTokenFromHttpHeaders(HttpHeaders httpHeaders) {
        if (!httpHeaders.containsKey("authorization")) { //HttpHeaders.AUTHORIZATION)) {
//            this.bearerRef.getAndUpdate(Bearer::reset);
            return;
        }

        Stream.of(httpHeaders.get(HttpHeaders.AUTHORIZATION))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(BEARER_PATTERN.asPredicate())
                .findFirst()
                .ifPresent(this::saveJsonWebToken);
    }

    //
    private void saveJsonWebToken(String authorizationBearer) {
        this.headers.addGlobalRequestHeader("authorization", authorizationBearer);
    }
}
