package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import lombok.Builder;

import java.util.Map;

@Builder
public record Race(
        String srdKey,
        String name,
        Map<RaceTraitsParser.RaceTraitsKey, Object> traits
) {
}
