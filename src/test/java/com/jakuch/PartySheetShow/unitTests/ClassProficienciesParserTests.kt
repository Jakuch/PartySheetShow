package com.jakuch.PartySheetShow.unitTests

import com.jakuch.PartySheetShow.open5e.dataParser.ClassProficienciesParser
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies
import com.jakuch.PartySheetShow.player.character.model.AbilityName
import com.jakuch.PartySheetShow.player.character.model.skill.SkillName
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClassProficienciesParserTests {

    private var classProficienciesParser: ClassProficienciesParser = ClassProficienciesParser()

    companion object {
        @JvmStatic
        private fun classProficienciesTestCases(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(
                            "**Armor:** Light armor, medium armor, shields\r\n**Weapons:** Simple weapons, martial weapons\r\n**Tools:** None\r\n**Saving Throws:** Strength, Dexterity\r\n**Skills:** Choose three from Animal Handling, Athletics, Insight, Investigation, Nature, Perception, Stealth, and Survival",
                            ClassProficiencies.builder()
                                    .armor(listOf("Light armor", "medium armor", "shields"))
                                    .weapons(listOf("Simple weapons", "martial weapons"))
                                    .tools(emptyList())
                                    .savingThrows(listOf(AbilityName.STRENGTH, AbilityName.DEXTERITY))
                                    .skillProficiencies(listOf(SkillName.ANIMAL_HANDLING, SkillName.ATHLETICS, SkillName.INSIGHT,
                                            SkillName.INVESTIGATION, SkillName.NATURE, SkillName.PERCEPTION, SkillName.STEALTH, SkillName.SURVIVAL))
                                    .skillProficienciesChoseCount(3)
                                    .build()
                    ),
                    Arguments.of(
                            "**Armor:** Light armor, medium armor, shields\n\n**Weapons:** Simple weapons, martial weapons\n\n**Tools:** Tinker tools and two additional tools your choice\n\n**Saving Throws:** CON, INT\n\n**Skills:** Choose two from Arcana, History, Investigation, Perception, and Sleight of Hand",
                            ClassProficiencies.builder()
                                    .armor(listOf("Light armor", "medium armor", "shields"))
                                    .weapons(listOf("Simple weapons", "martial weapons"))
                                    .tools(listOf("Tinker tools and two additional tools your choice"))
                                    .savingThrows(listOf(AbilityName.CONSTITUTION, AbilityName.INTELLIGENCE))
                                    .skillProficiencies(listOf(SkillName.ARCANA, SkillName.HISTORY, SkillName.INVESTIGATION, SkillName.PERCEPTION, SkillName.SLEIGHT_OF_HAND))
                                    .skillProficienciesChoseCount(2)
                                    .build()
                    ),
                    Arguments.of(
                            "**Armor:** Light armor\r\n**Weapons:** Simple weapons, hand crossbows, longswords, rapiers, shortswords\r\n**Tools:** Three musical instruments of your choice\r\n**Saving Throws:** Dexterity, Charisma\r\n**Skills:** Choose any three",
                            ClassProficiencies.builder()
                                    .armor(listOf("Light armor"))
                                    .weapons(listOf("Simple weapons", "hand crossbows", "longswords", "rapiers", "shortswords"))
                                    .tools(listOf("Three musical instruments of your choice"))
                                    .savingThrows(listOf(AbilityName.DEXTERITY, AbilityName.CHARISMA))
                                    .skillProficiencies(emptyList())
                                    .skillProficienciesChoseCount(3)
                                    .build()
                    )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("classProficienciesTestCases")
    fun `should parse class proficiencies`(givenRawDescription: String, expectedClassProficiencies: ClassProficiencies) {
        // given when
        val mappedClassProficiencies = classProficienciesParser.mapToClassProficiencies(givenRawDescription)

        // then
        assertSoftly(mappedClassProficiencies) {
            armor() shouldContainExactlyInAnyOrder expectedClassProficiencies.armor()
            weapons() shouldContainExactlyInAnyOrder expectedClassProficiencies.weapons()
            tools() shouldContainExactlyInAnyOrder expectedClassProficiencies.tools()
            savingThrows() shouldContainExactlyInAnyOrder expectedClassProficiencies.savingThrows()
            skillProficiencies() shouldContainExactlyInAnyOrder expectedClassProficiencies.skillProficiencies()
            skillProficienciesChoseCount() shouldBe expectedClassProficiencies.skillProficienciesChoseCount()
        }
    }
}