package com.tgcs.tgcp.bridge;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PosbcBridgeApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PosbcBridgeApplication.class)
                .web(WebApplicationType.NONE)
                .headless(true)
                .registerShutdownHook(true)
                .build()
                .run(args);
    }

}
