package com.jakuch.IRequireOrder.srd.characterClass.service;

import com.jakuch.IRequireOrder.dice.model.DiceType;
import com.jakuch.IRequireOrder.level.model.Level;
import com.jakuch.IRequireOrder.srd.characterClass.model.CharacterClass;
import com.jakuch.IRequireOrder.srd.SrdFetcherServiceBase;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterClassFetcherService extends SrdFetcherServiceBase<CharacterClass> {
    public CharacterClassFetcherService(RestTemplate srdRestTemplate) {
        super(srdRestTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.open5e.com/v2/classes/";
    }

    @Override
    @Cacheable("classes")
    public List<CharacterClass> fetchAllMappedData() {
        return fetchAllData().stream().filter(jsonObject -> jsonObject.isNull("subclass_of")).map(jsonObject -> new CharacterClass(jsonObject.getString("name"), jsonObject.getString("key"))).collect(Collectors.toList());
    }

    @Override
    public CharacterClass fetchMappedSingleRecord(String srdKey) {
        var jsonObject = fetchSingleRecordByKey(srdKey);
        var characterClass = new CharacterClass(jsonObject.getString("name"), jsonObject.getString("key"));
        characterClass.setHitDice(DiceType.valueOf(jsonObject.getString("hit_dice").toUpperCase()));
        characterClass.setLevel(Level.FIRST);

        return characterClass;
    }

    @Override
    public CharacterClass fetchFullDataOfSingleRecord(String srdKey) {
        var jsonObject = fetchSingleRecordByKey(srdKey);
        return null;
    }
}
