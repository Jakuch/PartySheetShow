package com.jakuch.partySheetShow.initiativeTracker.repository;

import com.jakuch.partySheetShow.initiativeTracker.model.InitiativeTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InitiativeTrackerRepository extends MongoRepository<InitiativeTracker, String> {
}
