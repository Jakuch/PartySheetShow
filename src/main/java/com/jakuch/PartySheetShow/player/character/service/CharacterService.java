package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterRaceForm;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.CharacterClass;
import com.jakuch.PartySheetShow.player.character.model.Race;
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
    private RaceTraitsParser raceTraitsParser;

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
        var character = characterMapper.addNew(characterForm);
        var currentUser = appUserService.getCurrentUser();
        var accessRules = AccessRules.builder()
                .ownerId(currentUser.getId())
                .build();
        character.setAccessRules(accessRules);
        character.setPlayerName(currentUser.getUsername());

        characterRepository.save(character);
    }

    public void updateCharacter(String id, CharacterForm characterForm) {
        var character = characterRepository.findById(id);

        character.ifPresent(c -> {
            var updatedCharacter = characterMapper.update(characterForm, c);
            characterRepository.save(updatedCharacter);
        });
    }

    public void deleteCharacter(String id) {
        characterRepository.deleteById(id);
    }

    public void addClassToForm(CharacterForm characterForm, List<CharacterClass> classes) {
        var characterClass = classes.stream().filter(c -> characterForm.getChosenCharacterClassKey().equals(c.getSrdKey())).findFirst();
        characterClass.ifPresent(c ->
            characterForm.getClasses().put(c.getSrdKey(), CharacterClassForm.builder()
                    .key(c.getSrdKey())
                    .name(c.getName())
                    .level(c.getLevel().getNumericValue())
                    .build()));
    }

    public void addRaceToForm(CharacterForm characterForm, List<Race> races) {
        var race = races.stream().filter(r -> characterForm.getChosenRaceKey().equals(r.getSrdKey())).findFirst();
        race.ifPresent(r ->
            characterForm.setRace(CharacterRaceForm.builder()
                    .key(r.getSrdKey())
                    .name(r.getName())
                    .abilityBonuses(raceTraitsParser.parseAbilityIncrease(r))
                    .build()));
    }

    public void deleteClassFromForm(CharacterForm characterForm, String classKey) {
        if(!characterForm.getClasses().isEmpty()) {
            characterForm.getClasses().remove(classKey);
        }
    }

    public void deleteRaceFromForm(CharacterForm characterForm) {
        characterForm.setRace(null);
    }
}
