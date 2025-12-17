package com.jakuch.PartySheetShow.character.repository;

import com.jakuch.PartySheetShow.character.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, String> {
}
