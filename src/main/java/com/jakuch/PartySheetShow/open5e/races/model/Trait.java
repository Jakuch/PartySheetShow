package com.jakuch.PartySheetShow.open5e.races.model;

import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;

@Data
public class Trait extends Open5eData {

    private String type; //TODO change it to enum (there are only SIZE SPEED and NULL)

}
