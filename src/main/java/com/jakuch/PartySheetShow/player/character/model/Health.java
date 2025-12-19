package com.jakuch.PartySheetShow.player.character.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Health {
    private int current;
    private int max;
    private int temporary;
    private int hitDices; //dice type should be taken from Class
}
