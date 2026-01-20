package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public abstract class Open5eServiceBase<T> {

    protected final Open5eClient open5eClient;
    protected final Open5eProperties open5eProperties;
    protected final String path;
    protected final ParameterizedTypeReference<Open5eResponse<T>> type;
    protected final Class<T> singleType;

    public Open5eServiceBase(Open5eClient open5eClient, Open5eProperties open5eProperties, String path, ParameterizedTypeReference<Open5eResponse<T>> type, Class<T> singleType) {
        this.open5eClient = open5eClient;
        this.open5eProperties = open5eProperties;
        this.path = path;
        this.type = type;
        this.singleType = singleType;
    }

    public List<T> getAll() {
        return all(null);
    }

    public List<T> getAll(Map<String, ?> queryParams) {
        return all(queryParams);
    }

    public List<T> getSinglePage(int page) {
        var response = open5eClient.getPage(path, Map.of("page", page), type);
        return response.results();
    }

    public Optional<T> getByKey(String key) {
        try {
            T entity = open5eClient.getSingleRecord(path + key + "/", singleType);
            return Optional.of(entity);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    private List<T> all(Map<String, ?> queryParams) {
        log.info("Started fetching data from " + path + " Open5e endpoint.");

        var response = open5eClient.getPage(
                path,
                queryParams,
                type);

        var data = new ArrayList<>(response.results());
        var nextPageUrl = response.next();

        while (nextPageUrl != null) {
            var byUrl = open5eClient.getByUrl(nextPageUrl, type);

            nextPageUrl = byUrl.next();
            data.addAll(byUrl.results());
        }

        log.info("Finished fetching data from " + path + " Open5e endpoint.");
        return data;
    }
}
