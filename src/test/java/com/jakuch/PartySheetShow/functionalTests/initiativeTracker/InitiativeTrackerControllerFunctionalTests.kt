package com.jakuch.PartySheetShow.functionalTests.initiativeTracker

import com.jakuch.PartySheetShow.TestsBase
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeForm
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeTrackerForm
import com.jakuch.PartySheetShow.initiativeTracker.model.Initiative
import com.jakuch.PartySheetShow.initiativeTracker.model.InitiativeTracker
import com.jakuch.PartySheetShow.player.character.model.Character
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class InitiativeTrackerControllerFunctionalTests : TestsBase() {

    @Test
    fun `should show initiative tracker page`() {
        //given

        //when then
        mockMvc.perform(get("/initiativeTracker"))
                .andExpect(status().isOk)
                .andExpect(view().name("initiativeTracker"))
                .andExpect(model().attributeExists("initiativeTracker"))
    }

    @Test
    fun `should save initiative tracker and character`() {
        //given
        givenRepositoriesAreEmpty()

        //when
        mockMvc.perform(post("/initiativeTracker")
                .with(csrf())
                .param("saveTracker", "")
                .param("initiativeList[0].characterName", "TestChar"))
                .andExpect(view().name("initiativeTracker"))
                .andExpect(model().attributeExists("initiativeId"))

        //then
        initiativeTrackerRepository.findAll().size shouldBe 1
        characterRepository.findAll().size shouldBe 1
    }

    @Test
    fun `should load initiative tracker`() {
        //given
        givenCharactersAreSaved()
        givenInitiativeTrackerIsSaved()

        //when then
        mockMvc.perform(post("/initiativeTracker")
                .with(csrf())
                .param("loadTracker", "")
                .param("id", "1"))
                .andExpect(status().isOk)
                .andExpect(view().name("initiativeTracker"))
                .andExpect(model().attribute("initiativeTracker", givenInitiativeTrackerForm()))
    }

    @Test
    fun `should add row to initiative tracker`() {
        //given

        //when then
        mockMvc.perform(post("/initiativeTracker")
                .with(csrf())
                .param("addRow", ""))
                .andExpect(status().isOk)
                .andExpect(view().name("initiativeTracker"))
                .andExpect(model().attribute("initiativeTracker", givenInitiativeTrackerForm(
                        initiativeFormList = listOf(InitiativeForm())
                )))
    }

    @Test
    fun `should remove row from initiative tracker`() {
        //given

        //when then
        mockMvc.perform(post("/initiativeTracker")
                .with(csrf())
                .param("removeRow", "")
                .param("initiativeList[0].value", "10")
                .param("initiativeList[0].characterName", "Test One")
                .param("initiativeList[1].value", "5")
                .param("initiativeList[1].characterName", "Test Two"))
                .andExpect(status().isOk)
                .andExpect(view().name("initiativeTracker"))
                .andExpect(model().attribute("initiativeTracker", givenInitiativeTrackerForm(
                        initiativeFormList = listOf(InitiativeForm().apply {
                            value = 10
                            characterName = "Test One"
                        })
                )))
    }

    @Test
    fun `should sort tracker by initiative order`() {
        //given

        //when then
        mockMvc.perform(post("/initiativeTracker")
                .with(csrf())
                .param("sort", ""))
    }

    private fun givenRepositoriesAreEmpty() {
        initiativeTrackerRepository.findAll().size shouldBe 0
        givenCharacterRepositoryIsEmpty()
    }

    private fun givenInitiativeTrackerIsSaved() {
        val initiativeTracker = InitiativeTracker().apply {
            id = "1"
            initiative = listOf(
                    Initiative().apply {
                        value = 10
                        characterId = "1"
                    },
                    Initiative().apply {
                        value = 5
                        characterId = "2"
                    }
            )
        }
        initiativeTrackerRepository.save(initiativeTracker)
    }

    private fun givenCharactersAreSaved() {
        val characters = listOf(
                Character().apply {
                    id = "1"
                    name = "Test One"
                },
                Character().apply {
                    id = "2"
                    name = "Test Two"
                })
        characterRepository.saveAll(characters)
    }

    private fun givenInitiativeTrackerForm(initiativeFormList: List<InitiativeForm> = givenInitiativeFormList()): InitiativeTrackerForm {
        return InitiativeTrackerForm().apply {
            initiativeList = initiativeFormList
        }
    }

    private fun givenInitiativeFormList(): List<InitiativeForm> {
        return listOf(
                InitiativeForm().apply {
                    value = 10
                    characterName = "Test One"
                },
                InitiativeForm().apply {
                    value = 5
                    characterName = "Test Two"
                }
        )
    }
}