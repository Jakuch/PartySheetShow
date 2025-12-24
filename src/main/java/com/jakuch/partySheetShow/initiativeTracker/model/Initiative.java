package com.jakuch.partySheetShow.initiativeTracker.model;

import lombok.Data;

@Data
public class Initiative {

    private int value;
    private String characterId;
    private String notes;
    //TODO perhaps add timer for each player + movement (max+current)

}
