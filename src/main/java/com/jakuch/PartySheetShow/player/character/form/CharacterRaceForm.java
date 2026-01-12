package com.jakuch.PartySheetShow.player.character.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterRaceForm {
    private String key;
    private String name;
}
