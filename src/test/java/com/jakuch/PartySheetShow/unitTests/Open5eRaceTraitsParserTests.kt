package com.jakuch.PartySheetShow.unitTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.jakuch.PartySheetShow.open5e.dataParser.AbilitiesParser
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser.RaceTraitsKey
import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses
import com.jakuch.PartySheetShow.open5e.model.Open5eRace
import com.jakuch.PartySheetShow.player.character.model.AbilityName
import com.jakuch.PartySheetShow.player.character.model.Size
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Open5eRaceTraitsParserTests {

    private var raceTraitsParser: RaceTraitsParser = RaceTraitsParser(AbilitiesParser())
    private var objectMapper: ObjectMapper = ObjectMapper()

    companion object {

        @JvmStatic
        fun raceTraitTestCases(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(
                            givenRaceRawHuman(),
                            expectedHumanTraitsMap()
                    ),
                    Arguments.of(
                            givenRaceRawHalfElf(),
                            expectedHalfElfTraitsMap()
                    ),
                    Arguments.of(
                            givenRaceRawGearforged(),
                            expectedGearForgedTraits()
                    )
            )
        }

        private fun givenRaceRawHuman(): String {
            return "{\"name\":\"Human\",\"desc\":\"It’s hard to make generalizations about humans, but your human character has these traits.\",\"srdKey\":\"srd_human\",\"is_subspecies\":false,\"traits\":[{\"name\":\"Ability Score Increase\",\"type\":null,\"desc\":\"Your ability scores each increase by 1.\",\"srdKey\":null},{\"name\":\"Speed\",\"type\":null,\"desc\":\"Your base walking speed is 30 feet.\",\"srdKey\":null},{\"name\":\"Age\",\"type\":null,\"desc\":\"Humans reach adulthood in their late teens and live less than a century.\",\"srdKey\":null},{\"name\":\"Alignment\",\"type\":null,\"desc\":\"Humans tend toward no particular alignment. The best and the worst are found among them.\",\"srdKey\":null},{\"name\":\"Size\",\"type\":null,\"desc\":\"Humans vary widely in height and build, from barely 5 feet to well over 6 feet tall. Regardless of your position in that range, your size is Medium.\",\"srdKey\":null},{\"name\":\"Languages\",\"type\":null,\"desc\":\"You can speak, read, and write Common and one extra language of your choice. Humans typically learn the languages of other peoples they deal with, including obscure dialects. They are fond of sprinkling their speech with words borrowed from other tongues: Orc curses, Elvish musical expressions, Dwarvish military phrases, and so on.\",\"srdKey\":null}]}"
        }

        private fun expectedHumanTraitsMap(): Map<RaceTraitsKey, Any> {
            return mapOf(
                    RaceTraitsKey.ABILITY_INCREASE to AbilityBonuses.builder()
                            .fixed(AbilityName.values().associateWith { 1 })
                            .choices(emptyList())
                            .build(),
                    RaceTraitsKey.SIZE to Size.MEDIUM,
                    RaceTraitsKey.SPEED to 30,
                    RaceTraitsKey.SPECIFIC to mapOf(
                            "Alignment" to "Humans tend toward no particular alignment. The best and the worst are found among them.",
                            "Languages" to "You can speak, read, and write Common and one extra language of your choice. Humans typically learn the languages of other peoples they deal with, including obscure dialects. They are fond of sprinkling their speech with words borrowed from other tongues: Orc curses, Elvish musical expressions, Dwarvish military phrases, and so on.",
                            "Age" to "Humans reach adulthood in their late teens and live less than a century."
                    )
            )
        }

        private fun givenRaceRawHalfElf(): String {
            return "{\"name\":\"Half-Elf\",\"desc\":\"Your half-elf character has some qualities in common with elves and some that are unique to half-elves.\",\"srdKey\":\"srd_half-elf\",\"is_subspecies\":false,\"traits\":[{\"name\":\"Ability Score Increase\",\"type\":null,\"desc\":\"Your Charisma score increases by 2, and two other ability scores of your choice increase by 1.\",\"srdKey\":null},{\"name\":\"Speed\",\"type\":null,\"desc\":\"Your base walking speed is 30 feet.\",\"srdKey\":null},{\"name\":\"Darkvision\",\"type\":null,\"desc\":\"Thanks to your elf blood, you have superior vision in dark and dim conditions. You can see in dim light within 60 feet of you as if it were bright light, and in darkness as if it were dim light. You can't discern color in darkness, only shades of gray.\",\"srdKey\":null},{\"name\":\"Age\",\"type\":null,\"desc\":\"Half-elves mature at the same rate humans do and reach adulthood around the age of 20. They live much longer than humans, however, often exceeding 180 years.\",\"srdKey\":null},{\"name\":\"Alignment\",\"type\":null,\"desc\":\"Half-elves share the chaotic bent of their elven heritage. They value both personal freedom and creative expression, demonstrating neither love of leaders nor desire for followers. They chafe at rules, resent others' demands, and sometimes prove unreliable, or at least unpredictable.\",\"srdKey\":null},{\"name\":\"Size\",\"type\":null,\"desc\":\"Half-elves are about the same size as humans, ranging from 5 to 6 feet tall. Your size is Medium.\",\"srdKey\":null},{\"name\":\"Languages\",\"type\":null,\"desc\":\"You can speak, read, and write Common, Elvish, and one extra language of your choice.\",\"srdKey\":null},{\"name\":\"Fey Ancestry\",\"type\":null,\"desc\":\"You have advantage on saving throws against being charmed, and magic can't put you to sleep.\",\"srdKey\":null},{\"name\":\"Skill Versatility\",\"type\":null,\"desc\":\"You gain proficiency in two skills of your choice.\",\"srdKey\":null}]}"
        }

        private fun expectedHalfElfTraitsMap(): Map<RaceTraitsKey, Any> {
            return mapOf(
                    RaceTraitsKey.ABILITY_INCREASE to AbilityBonuses.builder()
                            .fixed(mapOf(AbilityName.CHARISMA to 2))
                            .choices(listOf(ChooseAny(2, 1, true)))
                            .build(),
                    RaceTraitsKey.SIZE to Size.MEDIUM,
                    RaceTraitsKey.SPEED to 30,
                    RaceTraitsKey.DARKVISION to 60,
                    RaceTraitsKey.SPECIFIC to mapOf(
                            "Alignment" to "Half-elves share the chaotic bent of their elven heritage. They value both personal freedom and creative expression, demonstrating neither love of leaders nor desire for followers. They chafe at rules, resent others' demands, and sometimes prove unreliable, or at least unpredictable.",
                            "Skill Versatility" to "You gain proficiency in two skills of your choice.",
                            "Languages" to "You can speak, read, and write Common, Elvish, and one extra language of your choice.",
                            "Fey Ancestry" to "You have advantage on saving throws against being charmed, and magic can't put you to sleep.",
                            "Age" to "Half-elves mature at the same rate humans do and reach adulthood around the age of 20. They live much longer than humans, however, often exceeding 180 years.",
                    )
            )
        }

        private fun givenRaceRawGearforged(): String {
            return "{\"name\":\"Gearforged\",\"desc\":\"The range of gearforged anatomy in all its variants is remarkable, but all gearforged share some common parts.\",\"srdKey\":\"toh_gearforged\",\"is_subspecies\":false,\"traits\":[{\"name\":\"Ability Score Increase\",\"type\":null,\"desc\":\"Two different ability scores of your choice increase by 1.\",\"srdKey\":null},{\"name\":\"Speed\",\"type\":null,\"desc\":\"Your base walking speed is determined by your Race Chassis.\",\"srdKey\":null},{\"name\":\"Age\",\"type\":null,\"desc\":\"The soul inhabiting a gearforged can be any age. As long as its new body is kept in good repair, there is no known limit to how long it can function.\",\"srdKey\":null},{\"name\":\"Alignment\",\"type\":null,\"desc\":\"No single alignment typifies gearforged, but most gearforged maintain the alignment they had before becoming gearforged.\",\"srdKey\":null},{\"name\":\"Size\",\"type\":null,\"desc\":\"Your size is determined by your Race Chassis.\",\"srdKey\":null},{\"name\":\"Languages\",\"type\":null,\"desc\":\"You can speak, read, and write Common, Machine Speech (a whistling, clicking language that's incomprehensible to non-gearforged), and a language associated with your Race Chassis.\",\"srdKey\":null},{\"name\":\"Construct Resilience\",\"type\":null,\"desc\":\"Your body is constructed, which frees you from some of the limits of fleshand- blood creatures. You have resistance to poison damage, you are immune to disease, and you have advantage on saving throws against being poisoned.\",\"srdKey\":null},{\"name\":\"Construct Vitality\",\"type\":null,\"desc\":\"You don't need to eat, drink, or breathe, and you don't sleep the way most creatures do. Instead, you enter a dormant state where you resemble a statue and remain semiconscious for 6 hours a day. During this time, the magic in your soul gem and everwound springs slowly repairs the day's damage to your mechanical body. While in this dormant state, you have disadvantage on Wisdom (Perception) checks. After resting in this way, you gain the same benefit that a human does from 8 hours of sleep.\",\"srdKey\":null},{\"name\":\"Living Construct\",\"type\":null,\"desc\":\"Your consciousness and soul reside within a soul gem to animate your mechanical body. As such, you are a living creature with some of the benefits and drawbacks of a construct. Though you can regain hit points from spells like cure wounds, you can also be affected by game effects that specifically target constructs, such as the shatter spell. As long as your soul gem remains intact, game effects that raise a creature from the dead work on you as normal, repairing the damaged pieces of your mechanical body (restoring lost sections only if the spell normally restores lost limbs) and returning you to life as a gearforged. Alternatively, if your body is destroyed but your soul gem and memory gears are intact, they can be installed into a new body with a ritual that takes one day and 10,000 gp worth of materials. If your soul gem is destroyed, only a wish spell can restore you to life, and you return as a fully living member of your original race.\",\"srdKey\":null},{\"name\":\"Race Chassis\",\"type\":null,\"desc\":\"Four races are known to create unique gearforged, building mechanical bodies similar in size and shape to their people: dwarf, gnome, human, and kobold. Choose one of these forms.\",\"srdKey\":null}]}"
        }

        private fun expectedGearForgedTraits(): Map<RaceTraitsKey, Any> {
            return mapOf(
                    RaceTraitsKey.ABILITY_INCREASE to AbilityBonuses.builder()
                            .fixed(emptyMap())
                            .choices(listOf(ChooseAny(2, 1, true)))
                            .build(),
                    RaceTraitsKey.SIZE to Size.MEDIUM,
                    RaceTraitsKey.SPEED to 30,
                    RaceTraitsKey.SPECIFIC to mapOf(
                            "Alignment" to "No single alignment typifies gearforged, but most gearforged maintain the alignment they had before becoming gearforged.",
                            "Race Chassis" to "Four races are known to create unique gearforged, building mechanical bodies similar in size and shape to their people: dwarf, gnome, human, and kobold. Choose one of these forms.",
                            "Construct Resilience" to "Your body is constructed, which frees you from some of the limits of fleshand- blood creatures. You have resistance to poison damage, you are immune to disease, and you have advantage on saving throws against being poisoned.",
                            "Languages" to "You can speak, read, and write Common, Machine Speech (a whistling, clicking language that's incomprehensible to non-gearforged), and a language associated with your Race Chassis.",
                            "Construct Vitality" to "You don't need to eat, drink, or breathe, and you don't sleep the way most creatures do. Instead, you enter a dormant state where you resemble a statue and remain semiconscious for 6 hours a day. During this time, the magic in your soul gem and everwound springs slowly repairs the day's damage to your mechanical body. While in this dormant state, you have disadvantage on Wisdom (Perception) checks. After resting in this way, you gain the same benefit that a human does from 8 hours of sleep.",
                            "Living Construct" to "Your consciousness and soul reside within a soul gem to animate your mechanical body. As such, you are a living creature with some of the benefits and drawbacks of a construct. Though you can regain hit points from spells like cure wounds, you can also be affected by game effects that specifically target constructs, such as the shatter spell. As long as your soul gem remains intact, game effects that raise a creature from the dead work on you as normal, repairing the damaged pieces of your mechanical body (restoring lost sections only if the spell normally restores lost limbs) and returning you to life as a gearforged. Alternatively, if your body is destroyed but your soul gem and memory gears are intact, they can be installed into a new body with a ritual that takes one day and 10,000 gp worth of materials. If your soul gem is destroyed, only a wish spell can restore you to life, and you return as a fully living member of your original race.",
                            "Age" to "The soul inhabiting a gearforged can be any age. As long as its new body is kept in good repair, there is no known limit to how long it can function."
                    )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("raceTraitTestCases")
    fun `should parse race traits`(raceRaw: String, expectedTraitsMap: Map<RaceTraitsKey, Any>) {
        val race = objectMapper.readValue(raceRaw, Open5eRace::class.java)

        //given when
        val parsedTraits = raceTraitsParser.parseRaceTraits(race)

        //then
        assertSoftly(parsedTraits) {
            keys shouldContainExactlyInAnyOrder expectedTraitsMap.keys
            values shouldContainExactlyInAnyOrder expectedTraitsMap.values
        }
    }
}