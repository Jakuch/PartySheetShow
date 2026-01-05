package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.attributes.model.Attribute;
import com.jakuch.PartySheetShow.open5e.attributes.model.Skill;
import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.races.model.Race;
import com.jakuch.PartySheetShow.player.character.model.savingThrows.SavingThrow;
import com.jakuch.PartySheetShow.player.level.model.Level;
import com.jakuch.PartySheetShow.security.model.AccessRules;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Data
public class Character {
    @Id
    private String id;
    private AccessRules accessRules;
    private String campaignId; //TODO NOT IMPLEMENTED - specific campaign that character belongs to
    private String name;
    private Health health;
    private int armorClass;
    private int walkingSpeed; //TODO remove moved to Race when it works (in traits)
    private InitiativeBonus initiativeBonus;
    private List<Attribute> attributes = new ArrayList<>();
    private Level level;
    private int currentExperiencePoints;
    private List<SavingThrow> savingThrows = new ArrayList<>();
    private List<CharacterClass> characterClasses = new ArrayList<>();
    private Race race;
    // private PassiveSenses passiveSenses;

    // private AdditionalInformation additionalInformation; TODO add here background, player, alignment, etc.
    // private Proficiencies proficiencies
    // private List<Object> customData; TODO move it to AdditionalInformation when its implemented

    public Attribute getAttribute(String srdKey) {
        return this.attributes.stream().filter(a -> srdKey.equalsIgnoreCase(a.getSrdKey())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing attribute"));
    }

    public Skill getSkill(String srdKey) {
        return this.attributes.stream().flatMap(attribute -> attribute.getSkills().stream().filter(skill -> srdKey.equalsIgnoreCase(skill.getSrdKey()))).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing skill"));
    }

    public void addCustomSkill(Skill skill) {
        //TODO
    }

    public void removeCustomSkill(String name) {
        //TODO
    }
}
