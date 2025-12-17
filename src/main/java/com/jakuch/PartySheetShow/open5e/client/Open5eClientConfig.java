package com.jakuch.PartySheetShow.open5e.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Open5eClientConfig {

    @Bean
    public RestClient open5eRestClient() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMinutes(10));
        factory.setReadTimeout(Duration.ofMinutes(10));

        return RestClient.builder()
                .baseUrl("https://api.open5e.com/v2")
                .requestFactory(factory)
                .build();
    }
}
