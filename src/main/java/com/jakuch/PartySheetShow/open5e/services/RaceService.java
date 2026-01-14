package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.player.character.model.Race;
import com.jakuch.PartySheetShow.player.character.model.RaceTrait;
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.RACE;

@Service
public class RaceService extends Open5eServiceBase<Race> {

    public RaceService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties,"/species/", RACE, Race.class);
    }

    @Override
    @Cacheable("racesAll")
    public List<Race> getAll() {
        return super.getAll();
    }

    public List<RaceTrait> getAllRaceTraits() {
        return getAll().stream().flatMap(race -> race.getRaceTraits().stream()).toList();
    }

    public List<RaceTrait> getAllAbilityImprovements() {
        return getAll().stream()
                .flatMap(race -> race.getRaceTraits().stream())
                .filter(trait -> trait.getName().equalsIgnoreCase(RaceTraitsParser.RaceTraitsKey.ABILITY_INCREASE.getSrdName())).toList();
    }
}
