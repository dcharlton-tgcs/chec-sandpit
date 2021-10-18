package com.tgcs.tgcp.bridge.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pos.printer")
@ConditionalOnProperty(value = "pos.printer.handle", havingValue = "true", matchIfMissing = false)
public class PrinterProperties {

    private String host = "localhost";
    private Integer port = 8187;

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


}
