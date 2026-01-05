package com.jakuch.PartySheetShow.initiativeTracker.service;

import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeForm;
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeTrackerForm;
import com.jakuch.PartySheetShow.initiativeTracker.model.InitiativeTracker;
import com.jakuch.PartySheetShow.initiativeTracker.repository.InitiativeTrackerRepository;
import com.jakuch.PartySheetShow.player.character.repository.CharacterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public InitiativeTrackerForm loadInitiativeTracker(String id) {
        var results = initiativeTrackerRepository.findById(id);
        var trackerForm = new InitiativeTrackerForm();
        results.ifPresent(initiativeTracker ->
                initiativeTracker.getInitiative().forEach(initiative -> {
                    var optionalCharacter = characterRepository.findById(initiative.getCharacterId());
                    optionalCharacter.ifPresent(character -> {
                        var initiativeForm = InitiativeForm.toForm(initiative, character);
                        trackerForm.getInitiativeList().add(initiativeForm);
                    });
                }));
        return trackerForm;
    }
}
