package com.jakuch.PartySheetShow.functionalTests.playerCharacter

import com.jakuch.PartySheetShow.TestsBase
import com.jakuch.PartySheetShow.player.character.model.Character
import com.jakuch.PartySheetShow.open5e.model.Open5eClass
import com.jakuch.PartySheetShow.open5e.model.Open5eRace
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class CharacterControllerFunctionalTests : TestsBase() {

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should return character list page for user`() {
        //given

        //when then
        mockMvc.perform(get("/characters"))
                .andExpect(status().isOk)
                .andExpect(view().name("characters"))
                .andExpect(model().attributeExists("characters"))
                .andExpect(model().attributeExists("abilityNames"))
    }

    @Test
    fun `should redirect to login page from characters`() {
        //given

        //when then
        mockMvc.perform(get("/characters"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    fun `should return characters belonging only to one campaign`() {
        TODO()
    }

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should show character sheet page`() {
        ///given
        givenCharacterInDatabase()

        //when then
        mockMvc.perform(post("/characters")
                .with(csrf())
                .param("characterSheet", "")
                .param("id", "1"))
                .andExpect(status().isOk)
                .andExpect(view().name("characterSheet"))
                .andExpect(model().attributeExists("character"))
    }

    @Test
    fun `should redirect to login page from characterSheet`() {
        //given

        //when then
        mockMvc.perform(post("/characters")
                .with(csrf())
                .param("characterSheet", "")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should show add character form page`() {
        //given
        Mockito.`when`(characterClassService.allClasses).thenReturn(listOf(Open5eClass()
                .apply { name = "Class" }))
        Mockito.`when`(raceService.all).thenReturn(listOf(Open5eRace()
                .apply { name = "Race" }))

        //when then
        mockMvc.perform(get("/characterAdd"))
                .andExpect(status().isOk)
                .andExpect(view().name("characterAdd"))
                .andExpect(model().attributeExists("character"))
                .andExpect(model().attributeExists("chosenClass"))
                .andExpect(model().attributeExists("chosenRace"))
    }

    @Test
    fun `should redirect to login page from characterAdd`() {
        //given

        //when then
        mockMvc.perform(get("/characterAdd"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should add character and redirect to characters page`() {
        //given
        givenCharacterRepositoryIsEmpty()

        //when
        mockMvc.perform(post("/characterAdd/submitForm")
                .with(csrf())
                .param("character", ""))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlPattern("**/characters"))
                .andExpect(view().name("characters"))
                .andExpect(model().attribute("characters",
                        listOf(Character().apply { name = "Test One" })))

        //then
        characterRepository.findAll().size shouldBe 1

    }

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should delete user character`() {
        //given
        givenCharacterInDatabase()

        //when
        mockMvc.perform(post("/characters")
                .with(csrf())
                .param("delete", "")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("redirect:/characters"))
                .andExpect(view().name("characters"))
                .andExpect(model().attribute("characters", emptyList<Character>()))

        //then
        characterRepository.findAll().size shouldBe 0
    }

    @Test
    fun `should not allow deleting character of different user`() {
        TODO()
    }

    @Test
    fun `should delete all characters owned by user for specific campaign`() {
        TODO()
    }

    private fun givenCharacterInDatabase() {
        val character = Character().apply {
            id = "1"
            name = "Test One"
        }
        characterRepository.save(character)
    }
}