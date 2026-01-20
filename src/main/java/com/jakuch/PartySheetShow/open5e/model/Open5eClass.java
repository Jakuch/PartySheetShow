package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.player.character.model.Level;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class Open5eClass extends Open5eData {

    private Level level = Level.FIRST; //TODO default for now to make it work

    @JsonProperty("subclass_of")
    private Open5eSubclass subclass;

    @JsonProperty("hit_dice")
    private DiceType hitDice;

    private List<Open5eFeature> features;

    private ClassProficiencies classProficiencies;

    public List<Open5eFeature> getClassLevelFeatures() {
        features.sort(Comparator.comparingInt(Open5eFeature::getLowestGainedAtLevel));
        return this.features.stream().filter(f -> "CLASS_LEVEL_FEATURE".equalsIgnoreCase(f.getType())).toList();
    }

    public Open5eFeature getClassProficienciesFeature() {
        return this.features.stream().filter(f -> "PROFICIENCIES".equalsIgnoreCase(f.getType())).findFirst()
                .orElse(null);
    }
}
