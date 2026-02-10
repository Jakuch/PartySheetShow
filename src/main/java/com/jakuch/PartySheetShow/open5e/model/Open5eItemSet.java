package com.jakuch.PartySheetShow.open5e.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Open5eItemSet extends Open5eData {
    private List<Open5eItem> items;
}
