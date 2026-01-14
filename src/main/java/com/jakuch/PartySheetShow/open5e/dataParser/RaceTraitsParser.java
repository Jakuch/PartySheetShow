package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.player.character.model.Race;
import com.jakuch.PartySheetShow.player.character.model.RaceTrait;
import com.jakuch.PartySheetShow.player.character.model.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class RaceTraitsParser {

    private AbilitiesParser abilityIncreaseParser;

    public Map<RaceTraitsKey, Object> parseRaceTraits(Race race) {
        var traitsMap = new HashMap<RaceTraitsKey, Object>();

        traitsMap.put(RaceTraitsKey.ABILITY_INCREASE, parseAbilityIncrease(race));
        traitsMap.put(RaceTraitsKey.SPEED, parseSpeedInFt(race));
        traitsMap.put(RaceTraitsKey.SIZE, parseSize(race));

        return traitsMap;
    }

    public int parseSpeedInFt(RaceTrait trait) {
        var speedValue = trait.getDescription().replaceAll("[^-?0-9]+", " ").trim();
        return Integer.parseInt(speedValue);
    }

    public int parseSpeedInFt(Race race) {
        var trait = filterTraitsBy(race.getRaceTraits(), RaceTraitsKey.SPEED);
        return trait.map(this::parseSpeedInFt).orElse(0);
    }

    public Size parseSize(RaceTrait trait) {
        var size = Arrays.stream(ParserHelper.removeSpecialCharacters(trait.getDescription())
                        .split(" "))
                .map(String::toUpperCase)
                .filter(upperCase ->  Size.asStringList().contains(upperCase))
                .findFirst()
                .orElse(Size.MEDIUM.name());
        return Size.valueOf(size);
    }

    public Size parseSize(Race race) {
        var trait = filterTraitsBy(race.getRaceTraits(), RaceTraitsKey.SIZE);
        return trait.map(this::parseSize).orElse(Size.MEDIUM);
    }

    public AbilityBonuses parseAbilityIncrease(RaceTrait trait) {
        return abilityIncreaseParser.parse(trait.getDescription());
    }

    public AbilityBonuses parseAbilityIncrease(Race race) {
        var trait = filterTraitsBy(race.getRaceTraits(), RaceTraitsKey.ABILITY_INCREASE);
        return trait.map(this::parseAbilityIncrease).orElse(new AbilityBonuses("", new HashMap<>(), List.of()));
    }

    private Optional<RaceTrait> filterTraitsBy(List<RaceTrait> traits, RaceTraitsKey key) {
        return traits.stream().filter(t -> t.getName().contains(key.srdName)).findFirst();
    }

    @Getter
    public enum RaceTraitsKey {
        ABILITY_INCREASE("Ability Score Increase"),
        SPEED("Speed"),
        SIZE("Size"),
        DARKVISION("Darkvision");

        private final String srdName;

        RaceTraitsKey(String srdName) {
            this.srdName = srdName;
        }
    }
}
