package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.player.character.model.SkillName;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

import static com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper.WORD_NUMBERS;

@Component
public class ProficienciesParser {

    private static final Pattern SECTION_PATTERN = Pattern.compile("\\*\\*\\s*(.+?)\\s*:\\s*\\*\\*\\s*(.+?)(?=(?:\\r?\\n)?\\*\\*\\s*.+?\\s*:\\s*\\*\\*|$)", Pattern.DOTALL);
    private static final Pattern CHOOSE_COUNT_PATTERN = Pattern.compile("choose\\s+(?:any\\s+)?(\\w+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SKILL_LIST_PATTERN = Pattern.compile("(?:from|:)\\s*(.+)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private Map<String, String> extractSections(String description) {
        if (description == null) return Map.of();

        var cleaned = description.replace("\r\n", "\n").trim();

        var sections = new LinkedHashMap<String, String>();
        var m = SECTION_PATTERN.matcher(cleaned);
        while (m.find()) {
            var key = m.group(1).trim();
            var value = m.group(2).trim();
            sections.put(key, value);
        }
        return sections;
    }

    private List<String> mapToList(String section) {
        if (section == null || section.isBlank()) return List.of();

        return Arrays.stream(section.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private List<AbilityName> parseSavingThrowsProficiencies(String value) {
        if (value == null || value.isBlank()) return List.of();

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(AbilityName::findByName)
                .toList();
    }

    private List<SkillName> mapSkillProficiencies(String section) {
        var matcher = SKILL_LIST_PATTERN.matcher(section);

        if(matcher.find()) {
            var skills = matcher.group(1)
                    .replace("*", "")
                    .replace(" and ", ", ")
                    .replaceAll(",", " ")
                    .trim();

            return Arrays.stream(skills.split(" "))
                    .map(String::trim)
                    .map(SkillName::fromName)
                    .flatMap(Optional::stream)
                    .toList();
        }
        return List.of();
    }

    public int getSkillProficienciesChooseCount(String section) {
        var matcher = CHOOSE_COUNT_PATTERN.matcher(section);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Cannot find choose count in: " + section);
        }

        return WORD_NUMBERS.getOrDefault(matcher.group(1), 0);
    }

    public ClassProficiencies mapToClassProficiencies(String description) {
        Map<String, String> sections = extractSections(description);

        List<String> armor = mapToList(sections.get("Armor"));
        List<String> weapons = mapToList(sections.get("Weapons"));
        List<String> tools = mapToList(sections.get("Tools"));
        List<AbilityName> savingThrows = parseSavingThrowsProficiencies(sections.get("Saving Throws"));
        List<SkillName> skills = mapSkillProficiencies(sections.get("Skills"));
        int skillProficienciesChooseCount = getSkillProficienciesChooseCount(sections.get("Skills"));

        return new ClassProficiencies(armor, weapons, tools, savingThrows, skills, skillProficienciesChooseCount);
    }
}
