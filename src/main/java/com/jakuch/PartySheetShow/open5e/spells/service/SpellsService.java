package com.jakuch.PartySheetShow.open5e.spells.service;

import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import com.jakuch.PartySheetShow.open5e.spells.model.Spell;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpellsService {

    private Open5eClient open5eClient;
    private static final ParameterizedTypeReference<Open5eResponse<Spell>> TYPE = new ParameterizedTypeReference<>() {};

    public SpellsService(Open5eClient open5eClient) {
        this.open5eClient = open5eClient;
    }

    public List<Spell> getAll() {
        List<Spell> spells = new ArrayList<>();

        int page = 1;
        while (true) {
            var response = open5eClient.getPage("/spells/", Map.of("page", page), TYPE);
            spells.addAll(response.results());

            if (response.next() == null) break;
            page++;
        }

        return spells;
    }

    public List<Spell> getSpellsPage(int page) {
        var response = open5eClient.getPage("/spells/", Map.of("page", page), TYPE);
        return response.results();
    }
}
