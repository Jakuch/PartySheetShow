package com.jakuch.PartySheetShow.open5e.races.service;

import com.jakuch.PartySheetShow.open5e.SrdFetcherServiceBase;
import com.jakuch.PartySheetShow.open5e.races.model.Race;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceFetcherService extends SrdFetcherServiceBase<Race> {
    public RaceFetcherService(RestTemplate srdRestTemplate) {
        super(srdRestTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.open5e.com/v2/races/";
    }

    @Override
    public List<Race> fetchAllMappedData() {
        return fetchAllData().stream().map(jsonObject -> new Race(jsonObject.getString("name"), jsonObject.getString("key"))).collect(Collectors.toList());
    }

    @Override
    public Race fetchMappedSingleRecord(String srdKey) {
        var jsonObject = fetchSingleRecordByKey(srdKey);
        return new Race(jsonObject.getString("name"), jsonObject.getString("key"));
    }

    @Override
    public Race fetchFullDataOfSingleRecord(String srdKey) {
        return new Race("TODO", "BRUH_ITS_NOT_IMPLEMENTED_YET");
    }

}
