package com.jakuch.PartySheetShow.player.character.form;

import com.jakuch.PartySheetShow.player.character.model.Level;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterForm {
    private String name;
    private int maxHealth;
    private int currentHealth;
    private int armorClass;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private Level level;
    private int walkingSpeed;
    private String chosenCharacterClassKey;
    private List<CharacterClassForm> characterClasses = new ArrayList<>();
    private String chosenRaceKey;
    private CharacterRaceForm race;
}
