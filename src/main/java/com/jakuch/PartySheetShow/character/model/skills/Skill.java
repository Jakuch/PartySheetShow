package com.jakuch.PartySheetShow.character.model.skills;

import com.jakuch.PartySheetShow.character.model.Advantage;
import com.jakuch.PartySheetShow.character.model.Proficiency;
import com.jakuch.PartySheetShow.character.model.attributes.AttributeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private String name;
    private int value;
    private Proficiency proficiency;
    private Advantage advantage;
    private AttributeName modifier;
}
