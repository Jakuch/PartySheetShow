package com.jakuch.IRequireOrder.srd.spells.service;

import com.jakuch.IRequireOrder.srd.SrdFetcherServiceBase;
import com.jakuch.IRequireOrder.srd.spells.model.Spell;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpellsFetcherService extends SrdFetcherServiceBase<Spell> {

    public SpellsFetcherService(RestTemplate srdRestTemplate) {
        super(srdRestTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.open5e.com/v2/spells/";
    }

    @Override
    @Cacheable("spells")
    public List<Spell> fetchAllMappedData() {
        return fetchAllData().stream().map(jsonObject -> new Spell(jsonObject.getString("name"), jsonObject.getString("key"))).collect(Collectors.toList());
    }

    @Override
    public Spell fetchMappedSingleRecord(String srdKey) {
        var jsonObject = fetchSingleRecordByKey(srdKey);
        return new Spell(jsonObject.getString("name"), jsonObject.getString("key"));
    }

    @Override
    public Spell fetchFullDataOfSingleRecord(String srdKey) {
        return null;
    }
}
