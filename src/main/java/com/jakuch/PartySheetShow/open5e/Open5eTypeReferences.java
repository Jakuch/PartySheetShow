package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import com.jakuch.PartySheetShow.open5e.model.*;
import org.springframework.core.ParameterizedTypeReference;

public class Open5eTypeReferences {

    public final static ParameterizedTypeReference<Open5eResponse<Open5eClass>> CHARACTER_CLASS = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eRace>> RACE = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eAbility>> ABILITY = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eSpell>> SPELL = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eItem>> ITEM = new ParameterizedTypeReference<>() {
    };
    public final static ParameterizedTypeReference<Open5eResponse<Open5eItemSet>> ITEM_SET = new ParameterizedTypeReference<>() {
    };

}
