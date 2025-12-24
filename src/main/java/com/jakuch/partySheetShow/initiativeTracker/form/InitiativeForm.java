package com.jakuch.partySheetShow.initiativeTracker.form;

import com.jakuch.partySheetShow.player.character.model.Character;
import com.jakuch.partySheetShow.initiativeTracker.model.Initiative;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
        character.setCurrentHealth(this.getCurrentHp());
        character.setMaxHealth(this.getMaxHp());
        return character;
    }

    public static InitiativeForm toDto(Initiative initiative, Character character) {
        var initiativeDto = new InitiativeForm();

        initiativeDto.setValue(initiative.getValue());
        initiativeDto.setNotes(initiative.getNotes());

        initiativeDto.setCharacterName(character.getName());
        initiativeDto.setArmorClass(character.getArmorClass());
        initiativeDto.setCurrentHp(character.getCurrentHealth());
        initiativeDto.setMaxHp(character.getMaxHealth());


        return initiativeDto;
    }
}
