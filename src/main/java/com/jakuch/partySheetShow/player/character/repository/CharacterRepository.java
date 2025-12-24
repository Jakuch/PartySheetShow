package com.jakuch.partySheetShow.player.character.repository;

import com.jakuch.partySheetShow.player.character.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, String> {
}
