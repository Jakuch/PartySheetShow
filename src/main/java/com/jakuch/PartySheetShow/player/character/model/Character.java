package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.player.character.model.skill.Initiative;
import com.jakuch.PartySheetShow.player.character.model.skill.PassiveSkill;
import com.jakuch.PartySheetShow.player.character.model.skill.SavingThrow;
import com.jakuch.PartySheetShow.player.character.model.skill.Skill;
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
    private String playerName;
    private Health health;
    private int armorClass;
    private int walkingSpeed; //it can be taken from multiple sources (like race or specific class/items properties) and added so let it stay
    private Size size;
    private Initiative initiative;
    private List<Ability> abilities = new ArrayList<>();
    private Level level;
    private int currentExperiencePoints;
    private List<SavingThrow> savingThrows = new ArrayList<>();
    private List<Open5eClass> characterClasses = new ArrayList<>();
    private Race race;
    private List<PassiveSkill> passiveSenses = new ArrayList<>();

    // private AdditionalInformation additionalInformation; TODO add here background, alignment, etc. + customData like notes, custom, familiars etc.

    public Ability getAbility(String srdKey) {
        return this.abilities.stream().filter(a -> srdKey.equalsIgnoreCase(a.getName().getSrdKey())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing ability"));
    }

    public Skill getSkill(String srdKey) {
        return this.abilities.stream().flatMap(ability -> ability.getSkills().stream().filter(skill -> srdKey.equalsIgnoreCase(skill.getSkillName().getSrdKey()))).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Missing skill"));
    }

    public String getSkillDisplayName(String srdKey) {
        return getSkill(srdKey).getSkillName().getDisplayName();
    }

    public String getSkillProficiencyShortName(String srdKey) {
        return getSkill(srdKey).getProficiency().getShortName();
    }

    public Advantage getSkillAdvantage(String srdKey) {
        return getSkill(srdKey).getAdvantage();
    }
}
