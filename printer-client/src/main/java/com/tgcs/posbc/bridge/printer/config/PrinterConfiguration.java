package com.tgcs.posbc.bridge.printer.config;

public class PrinterConfiguration {
    private String host = "localhost";
    private Integer port = 8187;

    public String getHost() {
        return host;
    }

    public PrinterConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public PrinterConfiguration setPort(Integer port) {
        this.port = port;
        return this;
    }

}
