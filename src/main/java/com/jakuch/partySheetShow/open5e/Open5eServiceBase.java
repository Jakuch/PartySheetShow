package com.jakuch.partySheetShow.open5e;

import com.jakuch.partySheetShow.open5e.client.Open5eClient;
import com.jakuch.partySheetShow.open5e.client.Open5eResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Open5eServiceBase<T> {

    protected final Open5eClient open5eClient;
    protected final String path;
    protected final ParameterizedTypeReference<Open5eResponse<T>> type;
    protected final Class<T> singleType;

    public Open5eServiceBase(Open5eClient open5eClient, String path, ParameterizedTypeReference<Open5eResponse<T>> type, Class<T> singleType) {
        this.open5eClient = open5eClient;
        this.path = path;
        this.type = type;
        this.singleType = singleType;
    }

    public List<T> getAll() {
        List<T> spells = new ArrayList<>();

        int page = 1;
        while (true) {
            var response = open5eClient.getPage(path, Map.of("page", page), type);
            spells.addAll(response.results());

            if (response.next() == null) break;
            page++;
        }

        return spells;
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
}
