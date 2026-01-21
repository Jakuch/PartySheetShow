package com.jakuch.PartySheetShow.player.character.form;

import com.jakuch.PartySheetShow.player.character.model.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterClassForm {
    //form fields
    private String key;
    private String name;
    private Level level;
    boolean isFirst;

    //sources

}
