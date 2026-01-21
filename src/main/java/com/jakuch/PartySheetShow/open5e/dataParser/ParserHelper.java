package com.jakuch.PartySheetShow.open5e.dataParser;

import java.util.Map;

public class ParserHelper {
    public static final Map<String, Integer> WORD_NUMBERS = Map.of(
            "one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6
    );

    public static String removeSpecialCharacters(String string) {
        return string.replaceAll("[^a-zA-Z0-9]+", " ");
    }

    public static Integer safeParseInt(String string, Integer defaultValue) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Integer safeParseInt(String string) {
        var s = removeSpecialCharacters(string);
        return safeParseInt(s, 0);
    }
}
