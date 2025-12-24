package com.jakuch.PartySheetShow.initiativeTracker.service;

import com.jakuch.PartySheetShow.player.character.repository.CharacterRepository;
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeForm;
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeTrackerForm;
import com.jakuch.PartySheetShow.initiativeTracker.model.InitiativeTracker;
import com.jakuch.PartySheetShow.initiativeTracker.repository.InitiativeTrackerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
@AllArgsConstructor
public class InitiativeTrackerService {

    private InitiativeTrackerRepository initiativeTrackerRepository;
    private CharacterRepository characterRepository;

    public String saveInitiativeTracker(InitiativeTrackerForm initiativeTrackerForm) {
        var initiativeTracker = new InitiativeTracker();
        initiativeTrackerForm.getInitiativeList()
                .forEach(el -> {
                    var id = characterRepository.save(el.toCharacter()).getId();
                    var initiative = el.toInitiative(id);

                    initiativeTracker.getInitiative().add(initiative);
                });
        return initiativeTrackerRepository.save(initiativeTracker).getId();
    }

    public InitiativeTrackerForm loadInitiativeTracker(String id) throws FileNotFoundException {
        var results = initiativeTrackerRepository.findById(id);
        if (results.isPresent()) {
            var initiativeTrackerForm = new InitiativeTrackerForm();
            var initiativeTracker = results.get();
            initiativeTracker.getInitiative().forEach(el ->
            {
                var optionalCharacter = characterRepository.findById(el.getCharacterId());
                if (optionalCharacter.isPresent()) {
                    var initiativeDto = InitiativeForm.toDto(el, optionalCharacter.get());
                    initiativeTrackerForm.getInitiativeList().add(initiativeDto);
                }
            });
            return initiativeTrackerForm;
        } else {
            throw new FileNotFoundException("Error there is no such tracker");
        }
    }

    public void deleteAll() {
        initiativeTrackerRepository.deleteAll();
    }
}
