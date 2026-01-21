package com.jakuch.PartySheetShow.open5e.dataParser;

import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.open5e.dataParser.model.Choice;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper.WORD_NUMBERS;

@Component
public class AbilitiesParser {

    private static final Pattern FIXED = Pattern.compile(
            "(?i)your\\s+([A-Za-z]+)\\s+score\\s+increases\\s+by\\s+(\\d+)"
    );

    private static final Pattern EACH = Pattern.compile(
            "(?i)your\\s+ability\\s+scores\\s+each\\s+increase\\s+by\\s+(\\d+)"
    );

    private static final Pattern CHOOSE_ANY = Pattern.compile(
            "(?i)(one|two|three|four|five|six|\\d+)\\s+(?:different\\s+)?(?:other\\s+)?ability\\s+scores?\\s+of\\s+your\\s+choice\\s+increases?\\s+by\\s+(\\d+)"
    );

    private static final Pattern CHOOSE_ANY_EXCEPT = Pattern.compile(
            "(?i)(one|two|three|four|five|six|\\d+)\\s+ability\\s+scores?\\s+of\\s+your\\s+choice,\\s+other\\s+than\\s+([A-Za-z]+),\\s+increases?\\s+by\\s+(\\d+)"
    );

    private static final Pattern CHOOSE_ONE_OF = Pattern.compile(
            "(?i)your\\s+([A-Za-z]+)\\s+or\\s+([A-Za-z]+)\\s+score\\s+increases\\s+by\\s+(\\d+)"
    );

    private static final Pattern CHOOSE_EITHER_OR = Pattern.compile(
            "(?i)choose\\s+to\\s+increase\\s+either\\s+your\\s+([A-Za-z]+)\\s+or\\s+([A-Za-z]+)\\s+score\\s+by\\s+(\\d+)"
    );

    public AbilityBonuses parse(String description) {
        var text = ParserHelper.removeSpecialCharacters(description);

        var fixed = new HashMap<AbilityName, Integer>();
        var choices = new ArrayList<Choice>();

        var mEach = EACH.matcher(text);
        if (mEach.find()) {
            int amount = Integer.parseInt(mEach.group(1));
            fixed.putAll(Arrays.stream(AbilityName.values()).collect(Collectors.toMap(
                    abilityName -> abilityName, abilityName -> amount
            )));
            text = removeMatch(text, mEach);
        }

        var mOr = CHOOSE_ONE_OF.matcher(text);
        if (mOr.find()) {
            var a = AbilityName.findByNameOrSrdKey(filterAbilityName(mOr.group(1)));
            var b = AbilityName.findByNameOrSrdKey(filterAbilityName(mOr.group(2)));
            int amount = Integer.parseInt(mOr.group(3));
            choices.add(new Choice(AbilityName.NONE, amount, List.of(a, b)));
            text = removeMatch(text, mOr);
        }

        var mEither = CHOOSE_EITHER_OR.matcher(text);
        if (mEither.find()) {
            var a = AbilityName.findByNameOrSrdKey(filterAbilityName(mEither.group(1)));
            var b = AbilityName.findByNameOrSrdKey(filterAbilityName(mEither.group(2)));
            int amount = Integer.parseInt(mEither.group(3));
            choices.add(new Choice(AbilityName.NONE, amount, List.of(a, b)));
            text = removeMatch(text, mEither);
        }

        var mExcept = CHOOSE_ANY_EXCEPT.matcher(text);
        if (mExcept.find()) {
            int count = parseNumber(mExcept.group(1));
            var options = new ArrayList<>(AbilityName.correctValues());
            options.remove(AbilityName.findByNameOrSrdKey(filterAbilityName(mExcept.group(2))));
            int amount = Integer.parseInt(mExcept.group(3));

            for (int i = 0; i < count; i++) {
                choices.add(new Choice(AbilityName.NONE, amount, options));
            }
            text = removeMatch(text, mExcept);
        }

        var mChooseAny = CHOOSE_ANY.matcher(text);
        if (mChooseAny.find()) {
            int count = parseNumber(mChooseAny.group(1));
            int amount = Integer.parseInt(mChooseAny.group(2));
            boolean different = text.toLowerCase().contains("different ability scores");
            boolean other = false;
            if (!different) {
                other = text.toLowerCase().contains("other ability scores");
            }
            if (other) {
                var mFixed = FIXED.matcher(text);
                if (mFixed.find()) {
                    var options = new ArrayList<>(AbilityName.correctValues());
                    options.remove(AbilityName.findByNameOrSrdKey(filterAbilityName(mFixed.group(1))));
                    for (int i = 0; i < count; i++) {
                        choices.add(new Choice(AbilityName.NONE, amount, options));
                    }
                }
            } else {
                for (int i = 0; i < count; i++) {
                    choices.add(new Choice(AbilityName.NONE, amount, AbilityName.correctValues()));
                }
            }
            text = removeMatch(text, mChooseAny);
        }

        var mFixed = FIXED.matcher(text);
        while (mFixed.find()) {
            var attr = AbilityName.findByNameOrSrdKey(filterAbilityName(mFixed.group(1)));
            int amount = Integer.parseInt(mFixed.group(2));
            fixed.merge(attr, amount, Integer::sum);
        }

        return AbilityBonuses.builder()
                .fixed(fixed)
                .choices(choices)
                .build();
    }

    private String filterAbilityName(String text) {
        String[] split = text.split(" ");
        return text;
    }

    private String removeMatch(String text, Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();
        return (text.substring(0, start) + " " + text.substring(end)).replaceAll("\\s+", " ").trim();
    }

    private int parseNumber(String number) {
        if (number.chars().allMatch(Character::isDigit)) {
            return Integer.parseInt(number);
        }
        return WORD_NUMBERS.getOrDefault(number.toLowerCase(), 0);
    }
}
