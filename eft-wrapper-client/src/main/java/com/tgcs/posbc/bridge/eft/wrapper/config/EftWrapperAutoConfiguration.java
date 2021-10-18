package com.tgcs.posbc.bridge.eft.wrapper.config;

import com.tgcs.posbc.bridge.eft.wrapper.client.EftWrapperClient;
import com.tgcs.posbc.bridge.eft.wrapper.client.EftWrapperClientImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({EftWrapperProperties.class})
public class EftWrapperAutoConfiguration {

    @Bean
    public EftWrapperClient eftWrapperClient(EftWrapperProperties properties){
        return new EftWrapperClientImpl(new RestTemplate(), properties);
    }

}
