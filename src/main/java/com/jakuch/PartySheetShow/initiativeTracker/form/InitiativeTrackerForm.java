package com.jakuch.PartySheetShow.initiativeTracker.form;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InitiativeTrackerForm {

    private List<InitiativeForm> initiativeList = new ArrayList<>();
}
