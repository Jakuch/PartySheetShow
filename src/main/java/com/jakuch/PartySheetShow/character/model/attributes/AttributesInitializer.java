package com.jakuch.PartySheetShow.character.model.attributes;

import java.util.Map;
import java.util.stream.Collectors;

public class AttributesInitializer {

    public static Map<String, Attribute> initializeDefaultAttributes() {
        return AttributeName.correctValues().stream()
                .collect(Collectors.toMap(Enum::name, attributeName -> new Attribute(0)));
    }
}
