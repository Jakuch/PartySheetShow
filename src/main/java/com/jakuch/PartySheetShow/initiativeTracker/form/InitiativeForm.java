package com.jakuch.PartySheetShow.initiativeTracker.form;

import com.jakuch.PartySheetShow.initiativeTracker.model.Initiative;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.Health;
import lombok.Data;

@Data
public class InitiativeForm {

    private String characterName;
    private int value;
    private int armorClass;
    private int currentHp;
    private int maxHp;
    private String notes;

    public Initiative toInitiative(String characterId) {
        Initiative initiative = new Initiative();
        initiative.setValue(this.getValue());
        initiative.setCharacterId(characterId);
        initiative.setNotes(this.getNotes());

        return initiative;
    }

    public Character toCharacter() {
        Character character = new Character();
        character.setName(this.getCharacterName());
        character.setArmorClass(this.getArmorClass());
        character.setHealth(
                Health.builder()
                        .current(this.getCurrentHp())
                        .max(this.getMaxHp())
                        .build()
        );
        return character;
    }

    public static InitiativeForm toForm(Initiative initiative, Character character) {
        var initiativeForm = new InitiativeForm();

        initiativeForm.setValue(initiative.getValue());
        initiativeForm.setNotes(initiative.getNotes());

        initiativeForm.setCharacterName(character.getName());
        initiativeForm.setArmorClass(character.getArmorClass());
        initiativeForm.setCurrentHp(character.getHealth().getCurrent());
        initiativeForm.setMaxHp(character.getHealth().getMax());

        return initiativeForm;
    }
}
