package com.jakuch.PartySheetShow.player.character.model;

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
    private int walkingSpeed; //it can be taken from multiple sources (like race or specific class/items properties) and added so let it stay
    private InitiativeBonus initiativeBonus;
    private List<Attribute> attributes = new ArrayList<>();
    private Level level;
    private int currentExperiencePoints;
    private List<SavingThrow> savingThrows = new ArrayList<>();
    private List<CharacterClass> characterClasses = new ArrayList<>();
    private Race race;
    // private PassiveSenses passiveSenses;

    // private AdditionalInformation additionalInformation; TODO add here background, player, alignment, etc.
    // private List<Object> customData; TODO move it to AdditionalInformation when its implemented

    public Attribute getAttribute(String srdKey) {
        return this.attributes.stream().filter(a -> srdKey.equalsIgnoreCase(a.getSrdKey())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing attribute"));
    }

    public Skill getSkill(String srdKey) {
        return this.attributes.stream().flatMap(attribute -> attribute.getSkills().stream().filter(skill -> srdKey.equalsIgnoreCase(skill.getSrdKey()))).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing skill"));
    }

    public Advantage getSkillAdvantage(String srdKey) {
        return getSkill(srdKey).getAdvantage();
    }

    public void addCustomSkill(Skill skill) {
        //TODO
    }

    public void removeCustomSkill(String name) {
        //TODO
    }
}
