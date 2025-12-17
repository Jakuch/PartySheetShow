package com.jakuch.PartySheetShow.open5e;

import org.bson.json.JsonObject;

public interface SrdDataMapperBase<D extends SrdData> {
    D mapToBaseSrdData(JsonObject jsonObject);
    D mapFullData(JsonObject jsonObject);
}
