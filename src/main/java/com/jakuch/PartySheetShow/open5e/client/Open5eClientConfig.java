package com.jakuch.PartySheetShow.open5e.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
                //for now using only 5e 2014 edition to make it less confusing, hence hacking
                .requestInterceptor(((request, body, execution) -> {
                    var originalUri = request.getURI();
                    var builder = UriComponentsBuilder.fromUri(originalUri);

                    if(!builder.build().getQueryParams().containsKey("document__gamesystem__key")) {
                        builder.queryParam("document__gamesystem__key", "5e-2014");
                    }

                    var newUri = builder.build().toUri();
                    var requestWrapper = new HttpRequestWrapper(request) {
                        @Override
                        public URI getURI() {
                            return newUri;
                        }
                    };

                    return execution.execute(requestWrapper, body);
                }))
                .requestFactory(factory)
                .build();
    }
}
