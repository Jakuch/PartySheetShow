package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import com.jakuch.PartySheetShow.player.character.model.Attribute;
import com.jakuch.PartySheetShow.player.character.model.CharacterClass;
import com.jakuch.PartySheetShow.player.character.model.Race;
import com.jakuch.PartySheetShow.player.character.model.Spell;
import org.springframework.core.ParameterizedTypeReference;

public class Open5eTypeReferences {

    public final static ParameterizedTypeReference<Open5eResponse<CharacterClass>> CHARACTER_CLASS = new ParameterizedTypeReference<>() {};
    public final static ParameterizedTypeReference<Open5eResponse<Race>> RACE = new ParameterizedTypeReference<>() {};
    public final static ParameterizedTypeReference<Open5eResponse<Attribute>> ATTRIBUTE = new ParameterizedTypeReference<>() {};
    public final static ParameterizedTypeReference<Open5eResponse<Spell>> SPELL = new ParameterizedTypeReference<>() {};

}
