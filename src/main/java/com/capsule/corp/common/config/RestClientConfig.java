package com.capsule.corp.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean(name = "transactionServiceClient")
    public RestClient transactionService(final AppConfiguration.TransactionServiceConfig config) {
        return RestClient.builder()
                .requestFactory(getClientHttpRequestFactory(config.getReadTimeout(), config.getConnectionTimeout()))
                .requestInterceptor(new WebExchangeLoggingInterceptor())
                .build();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory(final int readTimeOut, final int connectionTimeOut) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(readTimeOut);
        factory.setConnectTimeout(connectionTimeOut);
        return factory;
    }
}
