package com.jakuch.partySheetShow.player.character.form;

import com.jakuch.partySheetShow.player.level.model.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
    private List<String> characterClassKey = new ArrayList<>();
    private String raceKey;
}
