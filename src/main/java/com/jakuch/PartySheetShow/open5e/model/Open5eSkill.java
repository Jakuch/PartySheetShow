package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.model.Open5eData;
import lombok.Data;

@Data
public class Open5eSkill extends Open5eData {

    @JsonProperty("ability")
    private String modifier;

}
