package com.jakuch.PartySheetShow.character.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CharacterForm { //TODO its actually for removal Character class should be enough (so whole characterAdd revamp)
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
    private int level;
    private int walkingSpeed;
    private List<String> characterClassSrdKey = new ArrayList<>();
    private String raceSrdKey;
}
