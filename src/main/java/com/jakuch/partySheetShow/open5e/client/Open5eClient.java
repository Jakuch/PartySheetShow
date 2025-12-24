package com.jakuch.partySheetShow.open5e.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class Open5eClient {

    private final RestClient open5eRestClient;

    public Open5eClient(RestClient open5eRestClient) {
        this.open5eRestClient = open5eRestClient;
    }

    public <T> Open5eResponse<T> getPage(String path, Map<String, ?> queryParams, ParameterizedTypeReference<Open5eResponse<T>> type) {
        return open5eRestClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (queryParams != null) queryParams.forEach(uriBuilder::queryParam);
                    return uriBuilder.build();
                })
                .retrieve()
                .body(type);
    }

    public <T> T getSingleRecord(String path, Class<T> type) {
        return open5eRestClient.get()
                .uri(path)
                .retrieve()
                .body(type);
    }
}
