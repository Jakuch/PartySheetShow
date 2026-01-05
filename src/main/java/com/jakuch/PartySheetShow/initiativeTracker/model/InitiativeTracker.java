package com.jakuch.PartySheetShow.initiativeTracker.model;

import com.jakuch.PartySheetShow.security.model.AccessRules;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class InitiativeTracker {
    @Id
    private String id;

    private AccessRules accessRules;

    private String campaignId;

    private List<Initiative> initiative = new ArrayList<>();
}
