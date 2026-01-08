package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.player.character.model.AttributeName;
import com.jakuch.PartySheetShow.player.character.model.ClassProficiencies;
import com.jakuch.PartySheetShow.player.character.model.SkillName;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class ClassProficienciesParser {

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

    private List<AttributeName> parseSavingThrowsProficiencies(String value) {
        if (value == null || value.isBlank()) return List.of();

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(this::attributeFromText)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<AttributeName> attributeFromText(String s) {
        if (s == null) return Optional.empty();
        return switch (s.trim().toLowerCase()) {
            case "strength" -> Optional.of(AttributeName.STRENGTH);
            case "dexterity" -> Optional.of(AttributeName.DEXTERITY);
            case "constitution" -> Optional.of(AttributeName.CONSTITUTION);
            case "intelligence" -> Optional.of(AttributeName.INTELLIGENCE);
            case "wisdom" -> Optional.of(AttributeName.WISDOM);
            case "charisma" -> Optional.of(AttributeName.CHARISMA);
            default -> Optional.empty();
        };
    }

    private List<SkillName> mapSkillProficiencies(String section) {
        var matcher = SKILL_LIST_PATTERN.matcher(section);

        if(matcher.find()) {
            var skills = matcher.group(1)
                    .replace("*", "")
                    .replace(" and ", ", ")
                    .replace("and", "")
                    .trim();

            return Arrays.stream(skills.split(","))
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

        return toNumber(matcher.group(1));
    }

    private int toNumber(String word) {
        return switch (word.toLowerCase()) {
            case "one" -> 1;
            case "two" -> 2;
            case "three" -> 3;
            case "four" -> 4;
            case "five" -> 5;
            default -> throw new IllegalArgumentException("Unknown number word: " + word);
        };
    }

    public ClassProficiencies mapToClassProficiencies(String description) {
        Map<String, String> sections = extractSections(description);

        List<String> armor = mapToList(sections.get("Armor"));
        List<String> weapons = mapToList(sections.get("Weapons"));
        List<String> tools = mapToList(sections.get("Tools"));
        List<AttributeName> savingThrows = parseSavingThrowsProficiencies(sections.get("Saving Throws"));
        List<SkillName> skills = mapSkillProficiencies(sections.get("Skills"));
        int skillProficienciesChooseCount = getSkillProficienciesChooseCount(sections.get("Skills"));

        return new ClassProficiencies(armor, weapons, tools, savingThrows, skills, skillProficienciesChooseCount);
    }
}
