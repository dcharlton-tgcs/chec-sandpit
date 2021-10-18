package com.tgcs.tgcp.bridge.webservice;

import com.tgcs.tgcp.bridge.adapter.ChecAdapter;
import com.tgcs.tgcp.bridge.webservice.ngp.NgpRequestIdProvider;
import com.tgcs.tgcp.bridge.webservice.ngp.NgpSession;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.Context;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.WebRequestBody;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestwrapper.NgpRequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public abstract class RequestWrapper {

    private static final String urlPrefix = "http://";

    @Value("${web-service.address}")
    private String webServiceIp;

    @Autowired
    private WebServiceRequest service;

    protected MultiValueMap<String, String> headers = null;

    private WebRequestBody requestBody = null;

    protected NgpSession session;

    @Autowired
    private ApplicationContext applicationContext;

    public RequestWrapper() {
    }

    public WebRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(WebRequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public RequestWrapper setSession(NgpSession session) {
        this.session = session;
        return this;
    }

    public String getUrl() {
        String endpoint = getEndpoint();
        if (endpoint == null) {
            return webServiceIp;
        }
        endpoint = endpoint.startsWith("/") ? endpoint : "/" + endpoint;
        String prefix = webServiceIp.startsWith("http") ? "" : urlPrefix;
        return prefix + webServiceIp + endpoint;
    }

    public abstract String getEndpoint();

    public abstract HttpMethod getRequestMethod();

    public <T extends ChecAdapter> void buildRequestNoHeader() {
        buildRequestBody();
    }

    public <T extends ChecAdapter> void buildRequest(NgpSession session) {
        this.session = session;
        buildRequestHeader(session.getAuthorizationKey());
        buildRequestBody();
    }

    public <T extends ChecAdapter> void buildRequest(NgpSession session, T objectMapper) {
        this.session = session;
        if (session != null) {
            buildRequestHeader(session.getAuthorizationKey());
        }
        buildRequestBody(session, objectMapper);
    }

    public MultiValueMap<String, String> buildRequestHeader(String authorization) {
        this.headers = new HttpHeaders();
        this.headers.add(NgpRequestHeader.AUTHORIZATION.getValue(), authorization);
        return this.headers;
    }

    public <V extends WebRequestBody> V buildRequestBody() {
        return null;
    }

    public <T extends WebRequestBody> T buildRequestBody(NgpSession session, ChecAdapter objectMapper) {
        return null;
    }


    public <T> ResponseEntity<T> sendRequest(Class<T> responseType) throws Exception {
        ResponseEntity<T> response = service.sendRequest(getUrl(), getHeaders(), getRequestBody(), getRequestMethod(), responseType);
        return response;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(MultiValueMap<String, String> headers) {
        this.headers = headers;
    }

    public Context getContext() {
        return applicationContext.getBean(Context.class, NgpRequestIdProvider.getUniqRequestId());
    }
}
