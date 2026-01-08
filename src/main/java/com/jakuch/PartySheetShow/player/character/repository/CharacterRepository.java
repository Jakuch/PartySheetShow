package com.jakuch.PartySheetShow.player.character.repository;

import com.jakuch.PartySheetShow.player.character.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CharacterRepository extends MongoRepository<Character, String> {

    List<Character> findAllByAccessRulesOwnerId(String userId);
}
