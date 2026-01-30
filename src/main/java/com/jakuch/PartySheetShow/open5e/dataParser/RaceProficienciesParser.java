package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.open5e.dataParser.model.*;
import com.jakuch.PartySheetShow.open5e.model.Open5eRaceTrait;
import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import static com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper.NUMBER_TOKEN;

@Component
public class RaceProficienciesParser {

    private static final Pattern PROFICIENCY_PATTERN = Pattern.compile("\\b(proficien\\w*)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern PROFICIENCY_GAIN_PATTERN = Pattern.compile("(?i)\\byou\\s+(?:are|have|gain)\\s+proficien(?:t|cy|cies)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern SKILL_TOOL_ANY = Pattern.compile(
            "(?i)\\b(?:gain|have|are)\\s+proficien\\w*\\s+(?:in|with)\\s+" +
                    NUMBER_TOKEN + "\\s+(skills?|tools?)(?:\\s+or\\s+(skills?|tools?))?\\s+of\\s+your\\s+choice\\b"
    );
    private static final Pattern SKILL_TOOL_FROM_LIST = Pattern.compile(
            "(?is)\\b(?:gain|have|are)\\s+proficien\\w*\\s+(?:in|with)\\s+" +
                    "(?:" + NUMBER_TOKEN + "\\s+of\\s+the\\s+following\\s+)?" +
                    "([\\w'\\- ]*?(?:skills?|tools?|musical\\s+instruments?))" +
                    "(?:\\s+of\\s+your\\s+choice)?\\s*:\\s*(.+)$"
    );
    private static final Pattern SKILL_AND_TOOL_SPLIT = Pattern.compile(
            "(?i)\\b(?:gain|have|are)\\s+proficien\\w*\\s+(?:in|with)\\s+" +
                    NUMBER_TOKEN + "\\s+skills?\\s+and\\s+" +
                    NUMBER_TOKEN + "\\s+tools?\\s+of\\s+your\\s+choice\\b"
    );

    public ProficiencyBonuses mapToProficiencyBonuses(List<Open5eRaceTrait> traits) {
        var proficiencies = traits.stream().filter(this::isProficiency).toList();
        var fixed = new ArrayList<FixedBonus>();
        var choices = new ArrayList<Choice>();
        var additional = new HashMap<String, String>();
        var hasExpertise = new AtomicBoolean(false);

        proficiencies.forEach(trait -> {
            var description = trait.getDescription();

            var sntM = SKILL_AND_TOOL_SPLIT.matcher(description);
            if (sntM.find()) {
                var skillCount = ParserHelper.parseNumber(sntM.group(1));
                for (int i = 0; i < skillCount; i++) {
                    choices.add(SkillChoice.builder()
                            .options(List.of(SkillName.values()))
                            .build());
                }
                var toolCount = ParserHelper.parseNumber(sntM.group(2));
                for (int i = 0; i < toolCount; i++) {
                    choices.add(ToolChoice.builder()
                            .options(ToolName.tools())
                            .build());
                }
            }

            var stflM = SKILL_TOOL_FROM_LIST.matcher(description);
            if (stflM.find()) {
                var count = (stflM.group(1) != null) ? ParserHelper.parseNumber(stflM.group(1)) : 1;
                var category = stflM.group(2);
                var options = stflM.group(3);
                for (int i = 0; i < count; i++) {
                    if (category.contains("skill")) {
                        List<SkillName> list = Arrays.stream(ParserHelper.removeSpecialCharacters(options).split(" "))
                                .map(SkillName::fromNameOrSrdKey)
                                .flatMap(Optional::stream)
                                .toList();

                        choices.add(SkillChoice.builder()
                                .options(list)
                                .build());
                    }
                    if (category.contains("tool")) {
                        var tools = ToolName.toolsFromDescription(description);

                        choices.add(ToolChoice.builder()
                                .options(tools)
                                .build());
                    }
                    if (category.contains("musical instrument")) {
                        var instruments = ToolName.instrumentsFromDescription(description);

                        choices.add(ToolChoice.builder()
                                .options(instruments)
                                .build());
                    }
                }
            }

            var staM = SKILL_TOOL_ANY.matcher(description);
            if (staM.find()) {
                var count = ParserHelper.parseNumber(staM.group(1));
                var skill = staM.group(2);
                var tool = staM.group(3);
                for (int i = 0; i < count; i++) {
                    if (skill != null && tool != null) {
                        choices.add(SkillOrToolChoice.builder()
                                .toolsOptions(ToolName.tools())
                                .skillOptions(Arrays.stream(SkillName.values()).toList())
                                .build());
                    } else if (skill != null) {
                        choices.add(SkillChoice.builder()
                                .options(Arrays.stream(SkillName.values()).toList())
                                .build());
                    } else if (tool != null) {
                        choices.add(ToolChoice.builder()
                                .options(ToolName.tools())
                                .build());
                    }
                }
            }
            var pgM = PROFICIENCY_GAIN_PATTERN.matcher(description);
            if (pgM.find()) {
                var descriptionArray = description.split(" ");
                var fixedSkillsProficiencies = Arrays.stream(descriptionArray)
                        .map(SkillName::fromName)
                        .flatMap(Optional::stream)
                        .map(el ->
                                SkillFixedBonus.builder()
                                        .skillName(el)
                                        .proficiency(Proficiency.FULL)
                                        .build()
                        ).toList();

                var fixedToolProficiencies = Arrays.stream(descriptionArray)
                        .map(ToolName::toolsFromName)
                        .flatMap(Optional::stream)
                        .map(el ->
                                ToolFixedBonus.builder()
                                        .toolName(el)
                                        .proficiency(Proficiency.FULL)
                                        .build())
                        .toList();

                fixed.addAll(fixedSkillsProficiencies);
                fixed.addAll(fixedToolProficiencies);
            }

            if(description.toLowerCase().contains("your proficiency is doubled")) {
                hasExpertise.set(true);
            }

            if(fixed.isEmpty() && choices.isEmpty()) {
                additional.put(trait.getName(), description);
            }
        });

        return ProficiencyBonuses.builder()
                .fixed(fixed)
                .choices(choices)
                .additional(additional)
                .hasExpertise(hasExpertise.get())
                .build();
    }

    private List<SkillName> getSkillNames(String description) {
        return Arrays.stream(description.split(" ")).map(SkillName::fromName).flatMap(Optional::stream).toList();
    }

    public boolean isProficiency(Open5eRaceTrait trait) {
        var description = ParserHelper.removeSpecialCharacters(trait.getDescription());
        return PROFICIENCY_PATTERN.matcher(description).find();
    }

    public boolean isGainProficiency(Open5eRaceTrait trait) {
        var description = ParserHelper.removeSpecialCharacters(trait.getDescription());
        return PROFICIENCY_GAIN_PATTERN.matcher(description).find();
    }

}
