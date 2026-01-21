package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.open5e.model.Open5eData;
import com.jakuch.PartySheetShow.open5e.model.Open5eRace;
import com.jakuch.PartySheetShow.open5e.model.Open5eRaceTrait;
import com.jakuch.PartySheetShow.player.character.model.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RaceTraitsParser {

    private AbilitiesParser abilityIncreaseParser;

    public Map<RaceTraitsKey, Object> parseRaceTraits(Open5eRace open5eRace) {
        var traitsMap = new HashMap<RaceTraitsKey, Object>();
        var traitsFromRace = new ArrayList<>(open5eRace.getRaceTraits());

        traitsMap.put(RaceTraitsKey.ABILITY_INCREASE, parseAbilityIncrease(open5eRace));
        traitsFromRace.removeIf(trait -> trait.getName().contains(RaceTraitsKey.ABILITY_INCREASE.srdName));

        traitsMap.put(RaceTraitsKey.SPEED, parseSpeedInFt(open5eRace));
        traitsFromRace.removeIf(trait -> trait.getName().contains(RaceTraitsKey.SPEED.srdName));

        traitsMap.put(RaceTraitsKey.SIZE, parseSize(open5eRace));
        traitsFromRace.removeIf(trait -> trait.getName().contains(RaceTraitsKey.SIZE.srdName));

        filterTraitsByKey(open5eRace.getRaceTraits(), RaceTraitsKey.DARKVISION)
                .ifPresent(t -> {
                    traitsMap.put(RaceTraitsKey.DARKVISION, parseDarkvision(t));
                    traitsFromRace.remove(t);
                });

        traitsMap.put(RaceTraitsKey.SPECIFIC, traitsFromRace.stream().collect(Collectors.toMap(Open5eData::getName, Open5eData::getDescription)));

        return traitsMap;
    }

    private int parseDarkvision(Open5eRaceTrait trait) {
        return ParserHelper.safeParseInt(ParserHelper.removeSpecialCharacters(trait.getDescription())
                .replaceAll("[^-?0-9]+", " ")
                .trim(), 60);
    }

    private int parseDarkvision(Open5eRace open5eRace) {
        return filterTraitsByKey(open5eRace.getRaceTraits(), RaceTraitsKey.DARKVISION)
                .map(this::parseDarkvision)
                .orElse(0);
    }

    public int parseSpeedInFt(Open5eRaceTrait trait) {
        var speedValue = trait.getDescription().replaceAll("[^-?0-9]+", " ").trim();
        return ParserHelper.safeParseInt(speedValue, 30);
    }

    public int parseSpeedInFt(Open5eRace open5eRace) {
        var trait = filterTraitsByKey(open5eRace.getRaceTraits(), RaceTraitsKey.SPEED);
        return trait.map(this::parseSpeedInFt).orElse(0);
    }

    public Size parseSize(Open5eRaceTrait trait) {
        var size = Arrays.stream(ParserHelper.removeSpecialCharacters(trait.getDescription())
                        .split(" "))
                .map(String::toUpperCase)
                .filter(upperCase -> Size.asStringList().contains(upperCase))
                .findFirst()
                .orElse(Size.MEDIUM.name());
        return Size.valueOf(size);
    }

    public Size parseSize(Open5eRace open5eRace) {
        var trait = filterTraitsByKey(open5eRace.getRaceTraits(), RaceTraitsKey.SIZE);
        return trait.map(this::parseSize).orElse(Size.MEDIUM);
    }

    public AbilityBonuses parseAbilityIncrease(Open5eRaceTrait trait) {
        return abilityIncreaseParser.parse(trait.getDescription());
    }

    public AbilityBonuses parseAbilityIncrease(Open5eRace open5eRace) {
        return filterTraitsByKey(open5eRace.getRaceTraits(), RaceTraitsKey.ABILITY_INCREASE)
                .map(this::parseAbilityIncrease).orElse(AbilityBonuses.builder().build());
    }

    private Optional<Open5eRaceTrait> filterTraitsByKey(List<Open5eRaceTrait> traits, RaceTraitsKey key) {
        return traits.stream().filter(t -> t.getName().contains(key.srdName)).findFirst();
    }

    @Getter
    public enum RaceTraitsKey {
        ABILITY_INCREASE("Ability Score Increase"),
        SPEED("Speed"),
        SIZE("Size"),
        DARKVISION("Darkvision"),
        SPECIFIC("Specific");

        private final String srdName;

        RaceTraitsKey(String srdName) {
            this.srdName = srdName;
        }
    }
}
