package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Open5eSkill extends Open5eData {

    @JsonProperty("ability")
    private String modifier;

}
