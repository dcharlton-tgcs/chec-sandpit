package com.tgcs.posbc.bridge.eft.wrapper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

@ConfigurationProperties("pos.handle.eft.wrapper")
public class EftWrapperProperties {

    private String contextPath = "/";
    private String proto = "http";
    private String host = "localhost";
    private Integer port = 8181;
    private Integer connectTimeout = 10;
    private Integer readTimeout = 130;

    public EftWrapperProperties() {  }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getProto() {
        return proto;
    }

    public EftWrapperProperties setProto(String proto) {
        this.proto = proto;
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public EftWrapperProperties setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public EftWrapperProperties setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public UriComponentsBuilder urlBuilder() {
        return UriComponentsBuilder
                .newInstance()
                .scheme(proto)
                .host(host)
                .port(port)
                .path(contextPath);
    }
}
