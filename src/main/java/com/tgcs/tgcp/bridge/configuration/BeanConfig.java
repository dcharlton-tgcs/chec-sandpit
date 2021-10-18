package com.tgcs.tgcp.bridge.configuration;

import com.tgcs.tgcp.bridge.webservice.IWebService;
import com.tgcs.tgcp.bridge.webservice.ngp.NgpServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ServerSocket;

@Configuration
//@ComponentScan("com.tgcs.tgcp.pos.api.client")
@ComponentScan("com.tgcs.tgcp.framework")
@ComponentScan("com.tgcs.tgcp.authorization.api")
@ComponentScan("com.tgcs.tgcp.cash.management")
@ComponentScan("org.springframework.boot.test.context.filter")
@ComponentScan("com.tgcs.tgcp.pos.tpnet")
@ComponentScan("com.tgcs.tgcp.pos.connector.api")
public class BeanConfig {
    private static Logger logger = LoggerFactory.getLogger(BeanConfig.class);

    @Value("${tcp-server.handler.class-name}")
    private Class<? extends IWebService> serviceHandler;

    @Bean
    protected IWebService getWebServiceHandler() {
        IWebService webService;
        try {
            webService = serviceHandler.newInstance();
            logger.debug("WebService successfully instantiated from application file");
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Error instantiating class: " + serviceHandler, e);
            logger.error("Using default webService: NGP");
            webService = new NgpServiceHandler();
        }
        return webService;
    }

    @Bean
    protected RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    protected ServerSocket getServerSocket() throws IOException {
        return new ServerSocket();
    }

}
