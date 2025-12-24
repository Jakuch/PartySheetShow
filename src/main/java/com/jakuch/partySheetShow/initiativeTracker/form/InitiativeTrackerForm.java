package com.jakuch.partySheetShow.initiativeTracker.form;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InitiativeTrackerForm {

    private List<InitiativeForm> initiativeList = new ArrayList<>();
}
