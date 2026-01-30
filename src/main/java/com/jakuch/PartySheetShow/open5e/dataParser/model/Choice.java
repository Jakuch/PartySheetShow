package com.jakuch.PartySheetShow.open5e.dataParser.model;

import java.util.List;

public interface Choice<T> {
    void setChoice(T choice);
    T getChoice();
    List<T> getOptions();
}
