package com.jakuch.PartySheetShow.player.character.form;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.Level;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterForm {
    private String playerName;
    private String name;
    private int maxHealth;
    private int currentHealth;
    private int temporaryHealth;
    private int armorClass;
    private int currentExperiencePoints;
    @NotNull
    private Map<AbilityName, @Min(0) @Max(30) Integer> abilities = new HashMap<>();
    private Level level;
    private int walkingSpeed;
    private Map<String, CharacterClassForm> classes = new HashMap<>();
    private CharacterRaceForm race;

    //support form fields
    private String chosenCharacterClassKey;
    private String chosenRaceKey;
}
