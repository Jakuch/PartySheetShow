package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class SkillChoice implements Choice<SkillName> {
    private SkillName choice;
    private List<SkillName> options;

    @Override
    public void setChoice(SkillName choice) {
        this.choice = choice;
    }

    @Override
    public SkillName getChoice() {
        return choice;
    }

    @Override
    public List<SkillName> getOptions() {
        return options;
    }

}
