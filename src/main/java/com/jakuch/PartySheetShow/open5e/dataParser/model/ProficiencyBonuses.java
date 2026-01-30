package com.jakuch.PartySheetShow.open5e.dataParser.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProficiencyBonuses {
    private List<FixedBonus> fixed;
    private List<Choice> choices;
    private Map<String, String> additional;
    private boolean hasExpertise;
}
