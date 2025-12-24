package com.jakuch.PartySheetShow.security.repository;

import com.jakuch.PartySheetShow.security.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByUsername(String username);
}
