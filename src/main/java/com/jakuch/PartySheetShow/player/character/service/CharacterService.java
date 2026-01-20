package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.open5e.dataParser.model.choice.ChooseAny;
import com.jakuch.PartySheetShow.open5e.dataParser.model.choice.ChooseAnyExcept;
import com.jakuch.PartySheetShow.open5e.dataParser.model.choice.ChooseFrom;
import com.jakuch.PartySheetShow.open5e.dataParser.model.choice.ChooseOneOf;
import com.jakuch.PartySheetShow.open5e.model.Open5eRace;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterRaceForm;
import com.jakuch.PartySheetShow.player.character.mapper.CharacterMapper;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.player.character.repository.CharacterRepository;
import com.jakuch.PartySheetShow.security.model.AccessRules;
import com.jakuch.PartySheetShow.security.model.AppUser;
import com.jakuch.PartySheetShow.security.model.AppUserRole;
import com.jakuch.PartySheetShow.security.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterService {

    private CharacterRepository characterRepository;
    private CharacterMapper characterMapper;
    private AppUserService appUserService;
    private RaceService raceService;

    public List<Character> findAllForUser(AppUser appUser) {
        if (appUser.getRoles().contains(AppUserRole.ROLE_ADMIN)) {
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

    public void addClassToForm(CharacterForm characterForm, List<Open5eClass> classes) {
        var characterClass = classes.stream().filter(c -> characterForm.getChosenCharacterClassKey().equals(c.getSrdKey())).findFirst();
        characterClass.ifPresent(c ->
                characterForm.getClasses().put(c.getSrdKey(), CharacterClassForm.builder()
                        .key(c.getSrdKey())
                        .name(c.getName())
                        .level(c.getLevel())
                        .build()));
    }

    public void addRaceToForm(CharacterForm characterForm, List<Open5eRace> open5eRaces) {
        var race = open5eRaces.stream().filter(r -> characterForm.getChosenRaceKey().equals(r.getSrdKey())).findFirst();
        race.ifPresent(r -> {
            var abilityBonuses = raceService.getAbilityBonuses(r);
            characterForm.setRace(CharacterRaceForm.builder()
                    .key(r.getSrdKey())
                    .name(r.getName())
                    .abilityBonuses(abilityBonuses)
                    .abilityNamesSelection(getAbilityNameSelection(abilityBonuses))
                    .abilityBonusChoices(new ArrayList<>())
                    .build());
        });
    }

    private List<AbilityName> getAbilityNameSelection(AbilityBonuses abilityBonuses) {
        return abilityBonuses.getChoices().stream()
                .map(choice -> {
                    var abilityNames = new ArrayList<>(AbilityName.correctValues());
                    return switch (choice) {
                        case ChooseAny any -> abilityNames;
                        case ChooseAnyExcept anyExcept -> {
                            abilityNames.remove(anyExcept.excluded());
                            yield abilityNames;
                        }
                        case ChooseFrom from -> from.options();
                        case ChooseOneOf oneOf -> List.of(oneOf.a(), oneOf.b());
                    };
                })
                .flatMap(Collection::stream)
                .toList();
    }

    public void deleteClassFromForm(CharacterForm characterForm, String classKey) {
        if (!characterForm.getClasses().isEmpty()) {
            characterForm.getClasses().remove(classKey);
        }
    }

    public void deleteRaceFromForm(CharacterForm characterForm) {
        characterForm.setRace(null);
    }
}
