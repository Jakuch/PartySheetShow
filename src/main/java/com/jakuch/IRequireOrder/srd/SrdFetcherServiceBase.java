package com.jakuch.IRequireOrder.srd;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public abstract class SrdFetcherServiceBase<D extends SrdData> {

    private RestTemplate srdRestTemplate;

    public SrdFetcherServiceBase(RestTemplate srdRestTemplate) {
        this.srdRestTemplate = srdRestTemplate;
    }

    protected abstract String getBaseUrl();

    public abstract List<D> fetchAllMappedData();

    public abstract D fetchMappedSingleRecord(String srdKey);

    public abstract D fetchFullDataOfSingleRecord(String srdKey);

    public List<JSONObject> fetchAllData() {
        var nextPage = getBaseUrl();
        var allSpells = new ArrayList<JSONObject>();

        while (nextPage != null) {
            var response = srdRestTemplate.getForObject(nextPage, String.class);
            var jsonResponse = new JSONObject(response);

            var spells = jsonResponse.getJSONArray("results");
            for (int i = 0; i < spells.length(); i++) {
                allSpells.add(spells.getJSONObject(i));
            }

            nextPage = jsonResponse.isNull("next") ? null : jsonResponse.getString("next");
        }

        return allSpells;
    }

    public JSONObject fetchSingleRecordByKey(String key) {
        var restTemplate = new RestTemplate();
        var response = restTemplate.getForObject(getBaseUrl() + key, String.class);

        return new JSONObject(response);
    }
}
