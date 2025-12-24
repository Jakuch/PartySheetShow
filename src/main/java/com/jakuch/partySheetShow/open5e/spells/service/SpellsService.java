package com.jakuch.partySheetShow.open5e.spells.service;

import com.jakuch.partySheetShow.open5e.Open5eServiceBase;
import com.jakuch.partySheetShow.open5e.client.Open5eClient;
import com.jakuch.partySheetShow.open5e.client.Open5eResponse;
import com.jakuch.partySheetShow.open5e.spells.model.Spell;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class SpellsService extends Open5eServiceBase<Spell> {

    private final static ParameterizedTypeReference<Open5eResponse<Spell>> TYPE = new ParameterizedTypeReference<>() {
    };

    public SpellsService(Open5eClient open5eClient) {
        super(open5eClient, "/spells/", TYPE, Spell.class);
    }
}
