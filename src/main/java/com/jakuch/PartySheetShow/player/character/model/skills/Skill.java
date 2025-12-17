package com.jakuch.PartySheetShow.player.character.model.skills;

import com.jakuch.PartySheetShow.player.character.model.Advantage;
import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import com.jakuch.PartySheetShow.player.character.model.attributes.AttributeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private String name;
    private int value;
    private Proficiency proficiency;
    private Advantage advantage;
    private AttributeName modifier;

    public static List<Skill> initializeSkills() {
        var skills = new ArrayList<Skill>();

        // Strength-based skills
        skills.add(new Skill("Athletics", 0, Proficiency.NONE, Advantage.NONE, AttributeName.STRENGTH));

        // Dexterity-based skills
        skills.add(new Skill("Acrobatics", 0, Proficiency.NONE, Advantage.NONE, AttributeName.DEXTERITY));
        skills.add(new Skill("Sleight of Hand", 0, Proficiency.NONE, Advantage.NONE, AttributeName.DEXTERITY));
        skills.add(new Skill("Stealth", 0, Proficiency.NONE, Advantage.NONE, AttributeName.DEXTERITY));

        // Intelligence-based skills
        skills.add(new Skill("Arcana", 0, Proficiency.NONE, Advantage.NONE, AttributeName.INTELLIGENCE));
        skills.add(new Skill("History", 0, Proficiency.NONE, Advantage.NONE, AttributeName.INTELLIGENCE));
        skills.add(new Skill("Investigation", 0, Proficiency.NONE, Advantage.NONE, AttributeName.INTELLIGENCE));
        skills.add(new Skill("Nature", 0, Proficiency.NONE, Advantage.NONE, AttributeName.INTELLIGENCE));
        skills.add(new Skill("Religion", 0, Proficiency.NONE, Advantage.NONE, AttributeName.INTELLIGENCE));

        // Wisdom-based skills
        skills.add(new Skill("Animal Handling", 0, Proficiency.NONE, Advantage.NONE, AttributeName.WISDOM));
        skills.add(new Skill("Insight", 0, Proficiency.NONE, Advantage.NONE, AttributeName.WISDOM));
        skills.add(new Skill("Medicine", 0, Proficiency.NONE, Advantage.NONE, AttributeName.WISDOM));
        skills.add(new Skill("Perception", 0, Proficiency.NONE, Advantage.NONE, AttributeName.WISDOM));
        skills.add(new Skill("Survival", 0, Proficiency.NONE, Advantage.NONE, AttributeName.WISDOM));

        // Charisma-based skills
        skills.add(new Skill("Deception", 0, Proficiency.NONE, Advantage.NONE, AttributeName.CHARISMA));
        skills.add(new Skill("Intimidation", 0, Proficiency.NONE, Advantage.NONE, AttributeName.CHARISMA));
        skills.add(new Skill("Performance", 0, Proficiency.NONE, Advantage.NONE, AttributeName.CHARISMA));
        skills.add(new Skill("Persuasion", 0, Proficiency.NONE, Advantage.NONE, AttributeName.CHARISMA));

        return skills;
    }
}
