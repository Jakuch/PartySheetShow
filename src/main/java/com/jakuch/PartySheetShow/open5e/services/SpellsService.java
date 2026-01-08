package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.player.character.model.Spell;
import org.springframework.stereotype.Service;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.SPELL;

@Service
public class SpellsService extends Open5eServiceBase<Spell> {

    public SpellsService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties,"/spells/", SPELL, Spell.class);
    }

}
