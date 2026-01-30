package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.open5e.dataParser.model.SkillChoice;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ToolChoice;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

import static com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper.NUMBER_TOKEN;
import static com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper.WORD_NUMBERS;

@Component
public class ClassProficienciesParser {

    private static final Pattern SECTION_PATTERN = Pattern.compile("\\*\\*\\s*(.+?)\\s*:\\s*\\*\\*\\s*(.+?)(?=(?:\\r?\\n)?\\*\\*\\s*.+?\\s*:\\s*\\*\\*|$)", Pattern.DOTALL);
    private static final Pattern CHOOSE_SKILL_COUNT_PATTERN = Pattern.compile("choose\\s+(?:any\\s+)?(\\w+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern TOOL_CHOICE_PATTERN = Pattern.compile("(?i)\\b(?:choose\\s+)?" + NUMBER_TOKEN + "(?:\\s+\\w+)?" + "\\s*(?:choice)?\\b", Pattern.CASE_INSENSITIVE);
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
                .map(s -> s.replace("None", ""))
                .filter(s -> !s.isBlank())
                .toList();
    }

    private List<ToolName> mapTools(String section) {
        if (section == null || section.isBlank()) return List.of();
        return ToolName.fromDescription(section);
    }

    private int countTools(String section) {
        var matcher = TOOL_CHOICE_PATTERN.matcher(section);
        if (matcher.find()) {
            var count = matcher.group(1);
            return WORD_NUMBERS.getOrDefault(count.toLowerCase(), 0);
        }
        return 0;
    }

    private List<AbilityName> parseSavingThrowsProficiencies(String value) {
        if (value == null || value.isBlank()) return List.of();

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(AbilityName::findByNameOrSrdKey)
                .toList();
    }

    private List<SkillName> mapSkillProficiencies(String section) {
        var matcher = SKILL_LIST_PATTERN.matcher(section);

        if (matcher.find()) {
            var skills = matcher.group(1)
                    .replace("*", "")
                    .replace(" and ", ", ")
                    .replaceAll(",", " ")
                    .replace("none", "")
                    .trim();

            return Arrays.stream(skills.split(" "))
                    .map(String::trim)
                    .map(SkillName::fromName)
                    .flatMap(Optional::stream)
                    .toList();
        }
        return List.of();
    }

    public int getChooseCount(String section) {
        var matcher = CHOOSE_SKILL_COUNT_PATTERN.matcher(section);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Cannot find count in: " + section);
        }

        return WORD_NUMBERS.getOrDefault(matcher.group(1), 0);
    }

    public ClassProficiencies mapToClassProficiencies(String description) {
        Map<String, String> sections = extractSections(description);

        List<String> armor = mapToList(sections.get("Armor"));
        List<String> weapons = mapToList(sections.get("Weapons"));

        var toolsSection = sections.get("Tools");
        List<ToolName> tools = mapTools(toolsSection);
        var toolChoices = new ArrayList<ToolChoice>();
        int toolsCount = countTools(toolsSection);
        for (int i = 0; i < toolsCount; i++) {
            toolChoices.add(ToolChoice.builder()
                    .options(getToolOptions(toolsSection))
                    .build());
        }

        List<AbilityName> savingThrows = parseSavingThrowsProficiencies(sections.get("Saving Throws"));

        var skillsSection = sections.get("Skills");
        int skillCount = getChooseCount(skillsSection);
        var skillChoices = new ArrayList<SkillChoice>();
        for (int i = 0; i < skillCount; i++) {
            skillChoices.add(SkillChoice.builder()
                    .options(getSkillOptions(skillsSection))
                    .build());
        }

        return new ClassProficiencies(armor, weapons, tools, toolChoices, savingThrows, skillChoices);
    }

    private List<ToolName> getToolOptions(String toolsSection) {
        var results = new ArrayList<ToolName>();
        if (toolsSection.toLowerCase().contains("musical instrument") && toolsSection.toLowerCase().contains("tools")) {
            results.addAll(ToolName.instruments());
            results.addAll(ToolName.tools());
        } else if (toolsSection.toLowerCase().contains("musical instrument")) {
            results.addAll(ToolName.instruments());
        } else if (toolsSection.toLowerCase().contains("tools")) {
            results.addAll(ToolName.tools());
        }

        return results;
    }

    private List<SkillName> getSkillOptions(String section) {
        var skills = mapSkillProficiencies(section);

        if (skills.isEmpty()) {
            skills = Arrays.stream(SkillName.values()).toList();
        }
        return skills;
    }
}
