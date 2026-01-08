package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.repository.CharacterRepository;
import com.jakuch.PartySheetShow.security.model.AccessRules;
import com.jakuch.PartySheetShow.security.model.AppUser;
import com.jakuch.PartySheetShow.security.model.AppUserRole;
import com.jakuch.PartySheetShow.security.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterService {

    private CharacterRepository characterRepository;
    private CharacterMapper characterMapper;
    private AppUserService appUserService;

    public List<Character> findAllForUser(AppUser appUser) {
        if(appUser.getRoles().contains(AppUserRole.ROLE_ADMIN)) {
            return characterRepository.findAll();
        }

        return characterRepository.findAllByAccessRulesOwnerId(appUser.getId());
    }

    public Optional<Character> findById(String id) {
        return characterRepository.findById(id);
    }

    public void saveCharacter(CharacterForm characterForm) {
        var character = characterMapper.fromForm(characterForm);
        var accessRules = AccessRules.builder()
                .ownerId(appUserService.getCurrentUser().getId())
                .build();
        character.setAccessRules(accessRules);

        characterRepository.save(character);
    }

    public void deleteCharacter(String id) {
        characterRepository.deleteById(id);
    }
}
