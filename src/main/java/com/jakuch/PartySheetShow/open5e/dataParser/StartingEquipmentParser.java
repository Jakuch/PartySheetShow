package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.open5e.model.Open5eFeature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class StartingEquipmentParser {

    private static final Pattern OPTION_MARKER = Pattern.compile("(?i)\\(([A-Za-z])\\)");

    public List<String> parseStartingEquipment(Open5eFeature feature) {
        var normalizedDescription = normalize(feature.getDescription());
        var descriptionSplit = normalizedDescription.split("\n");

        Arrays.stream(descriptionSplit).forEach(part -> {
            var matcher = OPTION_MARKER.matcher(part);
            if(matcher.find()) {
                System.out.println(part);
            }
        });

        return List.of(descriptionSplit);
    }

    private String normalize(String description) {
        return description.replace("\r", "")
                .replaceAll("\\(\\*([a-c])\\*\\)", "($1)")
                .replaceAll("\\*\\*\\(([a-c])\\)\\*\\*", "($1)")
                .replaceAll("\\*\\(([a-c])\\)\\*", "($1)")
                .trim();
    }
}
