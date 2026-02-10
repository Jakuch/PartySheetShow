package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.player.character.model.FeatureType;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Open5eClass extends Open5eData {

    @JsonProperty("subclass_of")
    private Open5eSubclass subclass;

    @JsonProperty("hit_dice")
    private DiceType hitDice;

    private List<Open5eFeature> features;

    public List<Open5eFeature> getClassLevelFeatures() {
        features.sort(Comparator.comparingInt(Open5eFeature::getLowestGainedAtLevel));
        return features.stream().filter(f -> FeatureType.CLASS_LEVEL_FEATURE.name().equalsIgnoreCase(f.getType())).toList();
    }

    public Open5eFeature getStartingEquipment() {
        return features.stream().filter(f -> FeatureType.STARTING_EQUIPMENT.name().equalsIgnoreCase(f.getType())).findFirst()
                .orElse(null);
    }

    public Open5eFeature getClassProficienciesFeature() {
        return features.stream().filter(f -> FeatureType.PROFICIENCIES.name().equalsIgnoreCase(f.getType())).findFirst()
                .orElse(null);
    }
}
