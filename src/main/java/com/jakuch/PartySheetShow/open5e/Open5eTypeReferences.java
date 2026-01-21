package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import com.jakuch.PartySheetShow.open5e.model.Open5eAbility;
import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.open5e.model.Open5eRace;
import com.jakuch.PartySheetShow.player.character.model.Spell;
import org.springframework.core.ParameterizedTypeReference;

public class Open5eTypeReferences {

    public final static ParameterizedTypeReference<Open5eResponse<Open5eClass>> CHARACTER_CLASS = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eRace>> RACE = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eAbility>> ABILITY = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Spell>> SPELL = new ParameterizedTypeReference<>() {
    };

}
