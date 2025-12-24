package com.jakuch.partySheetShow.security.repository;

import com.jakuch.partySheetShow.security.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByUsername(String username);
}
