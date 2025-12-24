package com.jakuch.PartySheetShow.initiativeTracker.repository;

import com.jakuch.PartySheetShow.initiativeTracker.model.InitiativeTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InitiativeTrackerRepository extends MongoRepository<InitiativeTracker, String> {
}
