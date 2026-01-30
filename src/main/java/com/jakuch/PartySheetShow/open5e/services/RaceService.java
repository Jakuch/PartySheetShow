package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.open5e.model.Open5eRace;
import com.jakuch.PartySheetShow.player.character.model.Race;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.RACE;

@Service
public class RaceService extends Open5eServiceBase<Open5eRace> {

    private final RaceTraitsParser raceTraitsParser;
    private final ItemService itemService;

    public RaceService(Open5eClient open5eClient, Open5eProperties open5eProperties, RaceTraitsParser raceTraitsParser, ItemService itemService) {
        super(open5eClient, open5eProperties, "/species/", RACE, Open5eRace.class);
        this.raceTraitsParser = raceTraitsParser;
        this.itemService = itemService;
    }

    @Override
    @Cacheable("racesAll")
    public List<Open5eRace> getAll() {
        return super.getAll();
    }

    public Optional<Race> getMappedRaceByKey(String srdKey) {
        return getByKey(srdKey)
                .map(r -> Race.builder()
                        .srdKey(r.getSrdKey())
                        .name(r.getName())
                        .traits(raceTraitsParser.parseRaceTraits(r))
                        .build());
    }

    public AbilityBonuses getAbilityBonuses(Open5eRace race) {
        return raceTraitsParser.parseAbilityIncrease(race);
    }
}
