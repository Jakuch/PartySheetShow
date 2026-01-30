package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.model.Open5eSpell;
import org.springframework.stereotype.Service;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.SPELL;

@Service
public class SpellService extends Open5eServiceBase<Open5eSpell> {

    public SpellService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties, "/spells/", SPELL, Open5eSpell.class);
    }

}
