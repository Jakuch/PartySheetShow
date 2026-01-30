package com.jakuch.PartySheetShow.open5e.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Open5eAbility extends Open5eData {

    private List<Open5eSkill> skills;

}
