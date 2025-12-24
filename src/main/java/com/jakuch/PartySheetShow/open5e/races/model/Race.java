package com.jakuch.PartySheetShow.open5e.races.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Race extends Open5eData {

    @JsonProperty("size_raw")
    private SizeCategory size;

    private int walkingSpeed;
}
