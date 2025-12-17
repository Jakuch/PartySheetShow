package com.jakuch.PartySheetShow.open5e.races.model;

import com.jakuch.PartySheetShow.open5e.SrdData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Race extends SrdData {
    public Race(String name, String srdKey) {
        this.name = name;
        this.srdKey = srdKey;
    }
}
