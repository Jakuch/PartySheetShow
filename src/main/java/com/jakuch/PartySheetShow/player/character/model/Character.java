package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.player.character.model.attributes.Attribute;
import com.jakuch.PartySheetShow.player.character.model.savingThrows.SavingThrow;
import com.jakuch.PartySheetShow.player.character.model.skills.Skill;
import com.jakuch.PartySheetShow.player.level.model.Level;
import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.races.model.Race;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Character {
    @Id
    private String id;
    private String userId;
    private String campaignId; //TODO NOT IMPLEMENTED - specific campaign that character belongs to
    private String name;
    private int maxHealth; //TODO remove moved to Health
    private int currentHealth; //TODO remove moved to Health
    private int tempHealth; //TODO remove moved to Health
    private Health health;
    private int armorClass;
    private int walkingSpeed; //TODO remove moved to Race
    private InitiativeBonus initiativeBonus;
    private Map<String, Attribute> attributes = Attribute.initializeDefaultAttributes();
    private Level level;
    private int currentExperiencePoints;
    private List<Skill> skills = Skill.initializeSkills();
    private List<SavingThrow> savingThrows = SavingThrow.initializeSavingThrows();
    private List<CharacterClass> characterClasses = new ArrayList<>();
    private Race race;
    // private PassiveSenses passiveSenses;

    // private AdditionalInformation additionalInformation; TODO add here background, player, alignment, etc.
    // private Proficiencies proficiencies
    // private List<Object> customData; TODO move it to AdditionalInformation when its implemented

    public void addCustomSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeCustomSkill(String name) {
        var baseSkills = Skill.initializeSkills();
        if (baseSkills.stream().anyMatch(el -> el.getName().equals(name))) {
            throw new UnsupportedOperationException("You can't remove base skills!");
        } else {
            this.skills.removeIf(el -> el.getName().equals(name));
        }
    }


}
