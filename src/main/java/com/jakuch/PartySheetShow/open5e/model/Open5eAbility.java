package com.jakuch.PartySheetShow.open5e.model;

import lombok.Data;

import java.util.List;

@Data
public class Open5eAbility extends Open5eData {

    private List<Open5eSkill> skills;

}
