package com.jakuch.PartySheetShow.functionalTests.users

import com.jakuch.PartySheetShow.TestsBase
import com.jakuch.PartySheetShow.security.model.AppUser
import com.jakuch.PartySheetShow.security.model.AppUserRole
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class RegistrationControllerFunctionalTests : TestsBase() {

    @Test
    fun `should return registration form`() {
        //given

        //when then
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registrationForm"))
                .andExpect(model().attributeExists("availableRoles"))
    }

    @Test
    fun `should save user and redirect to login page`() {
        //given
        givenUserRepositoryIsEmpty()
        givenPasswordIsEncoded()

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "secret")
                .param("roles", AppUserRole.ROLE_PLAYER.name, AppUserRole.ROLE_DM.name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"))

        //then
        thenUserWasSavedInDatabase(givenUser())
    }

    @Test
    fun `should return error when password does not match`() {
        ///given
        givenUserRepositoryIsEmpty()

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "incorrect")
                .param("roles", AppUserRole.ROLE_PLAYER.name, AppUserRole.ROLE_DM.name))
                .andExpect(status().isOk)
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registrationForm", "confirmPassword"))

        //then
        appUsersRepository.findByUsername("username") shouldBe Optional.empty()
    }

    @Test
    fun `should return error when user already exists`() {
        //given
        givenUserPresentInDatabase(givenUser())

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "secret")
                .param("roles", AppUserRole.ROLE_PLAYER.name, AppUserRole.ROLE_DM.name))
                .andExpect(status().isOk)
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registrationForm", "username"))

        //then
        appUsersRepository.findAll().size shouldBe 1
    }

    @Test
    fun `should return error when roles was not selected`() {
        //given
        givenUserRepositoryIsEmpty()

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "secret")
                .param("roles", ""))
                .andExpect(status().isOk)
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registrationForm", "roles"))

        //then
        appUsersRepository.findAll().size shouldBe 0
    }

    private fun thenUserWasSavedInDatabase(appUser: AppUser) {
        val saved = appUsersRepository.findByUsername(appUser.username)
        saved.shouldBePresent {
            this.username shouldBeEqual appUser.username
            this.password shouldBeEqual "ENCODED"
            this.roles shouldContainAll appUser.roles
        }
    }

    private fun givenPasswordIsEncoded() {
        Mockito.`when`(passwordEncoder.encode("secret")).thenReturn("ENCODED")
    }
}