package com.jakuch.PartySheetShow.player.character.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Data
@NoArgsConstructor
public class CharacterClass extends Open5eData {

    private Level level = Level.FIRST; //TODO default for now to make it work

    @JsonProperty("subclass_of")
    private Subclass subclass;

    @JsonProperty("hit_dice")
    private DiceType hitDice;

    private List<Feature> features;

    private ClassProficiencies classProficiencies;

    public List<Feature> getClassLevelFeatures() {
        features.sort(Comparator.comparingInt(f -> f.getGainedAt().stream()
                .mapToInt(FeatureGainedAt::getLevel)
                .min()
                .orElse(0)));
        return this.features.stream().filter(f -> "CLASS_LEVEL_FEATURE".equalsIgnoreCase(f.getType())).toList();
    }

    public Feature getClassProficienciesFeature() {
        return this.features.stream().filter(f -> "PROFICIENCIES".equalsIgnoreCase(f.getType())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing proficiencies"));
    }

    public Feature getClassProficiencyBonusFeature() {
        return this.features.stream().filter(f ->"PROFICIENCY_BONUS".equalsIgnoreCase(f.getType())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing proficiency bonus list"));
    }
}
