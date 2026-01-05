package com.jakuch.PartySheetShow.open5e.races.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Race extends Open5eData {

    private List<Trait> traits;
}
