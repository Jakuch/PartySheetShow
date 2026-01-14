package com.jakuch.PartySheetShow.open5e.client;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class Open5eClient {

    private final RestClient open5eRestClient;
    private final Open5eProperties open5eProperties;

    public Open5eClient(RestClient open5eRestClient, Open5eProperties open5eProperties) {
        this.open5eRestClient = open5eRestClient;
        this.open5eProperties = open5eProperties;
    }

    public <T> Open5eResponse<T> getPage(String path, Map<String, ?> queryParams, ParameterizedTypeReference<Open5eResponse<T>> type) {
        return open5eRestClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    var baseQueryParams = getBaseQueryParams();
                    if (queryParams != null) {
                        baseQueryParams.putAll(queryParams);
                    }

                    baseQueryParams.forEach(uriBuilder::queryParam);
                    var uri = uriBuilder.build();
                    log.info("Sending request with uri: "+ uri);
                    return uri;
                })
                .retrieve()
                .body(type);
    }

    public <T> Open5eResponse<T> getByUrl(String url, ParameterizedTypeReference<Open5eResponse<T>> type) {
        var uri = URI.create(url);
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("Expected absolute URL, got: " + url);
        }
        log.info("Sending request with uri: "+ uri);

        return open5eRestClient.get()
                .uri(uri)
                .retrieve()
                .body(type);
    }

    public <T> T getSingleRecord(String path, Class<T> type) {
        log.info("Fetching single record from path: "+ path);
        return open5eRestClient.get()
                .uri(path)
                .retrieve()
                .body(type);
    }

    private HashMap<String, Object> getBaseQueryParams() {
        var baseQueryParams = new HashMap<String, Object>();
        baseQueryParams.put("limit", open5eProperties.getPagination());
        if(open5eProperties.isUseDefaultGamesystem()) {
            baseQueryParams.put("document__gamesystem__key", open5eProperties.getDefaultGamesystem());
        }
        return baseQueryParams;
    }

}
