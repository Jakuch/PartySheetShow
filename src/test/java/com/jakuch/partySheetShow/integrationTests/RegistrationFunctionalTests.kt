package com.jakuch.partySheetShow.integrationTests

import com.jakuch.partySheetShow.TestsBase
import com.jakuch.partySheetShow.security.model.AppUser
import com.jakuch.partySheetShow.security.model.UserRole
import com.jakuch.partySheetShow.security.repository.UsersRepository
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class RegistrationFunctionalTests : TestsBase() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var usersRepository: UsersRepository

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder


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
        givenDatabaseIsEmpty()
        givenPasswordIsEncoded()

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "secret")
                .param("roles", UserRole.PLAYER.name, UserRole.DM.name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"))

        //then
        thenUserWasSavedInDatabase(givenUser())
    }

    @Test
    fun `should return error when password does not match`() {
        ///given
        givenPasswordIsEncoded()
        givenDatabaseIsEmpty()

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "incorrect")
                .param("roles", UserRole.PLAYER.name, UserRole.DM.name))
                .andExpect(status().isOk)
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registrationForm", "confirmPassword"))

        //then
        usersRepository.findByUsername("username") shouldBe Optional.empty()
    }

    @Test
    fun `should return error when user already exists`() {
        //given
        givenPasswordIsEncoded()
        givenUserPresentInDatabase(givenUser())

        //when
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "username")
                .param("password", "secret")
                .param("confirmPassword", "secret")
                .param("roles", UserRole.PLAYER.name, UserRole.DM.name))
    }

    private fun givenUserPresentInDatabase(user: AppUser) {
        usersRepository.save(user)
    }

    @Test
    fun shouldAssignDefaultRoleWhenNoneSpecified() {

    }

    private fun givenUser(username: String = "username", password: String = "secret", roles: List<UserRole> = listOf(UserRole.PLAYER, UserRole.DM)): AppUser {
        return AppUser().apply {
            this.username = username
            this.password = password
            this.roles = roles
        }
    }

    private fun thenUserWasSavedInDatabase(appUser: AppUser) {
        val saved = usersRepository.findByUsername(appUser.username)
        saved.shouldBePresent {
            this.username shouldBeEqual appUser.username
            this.password shouldBeEqual "ENCODED"
            this.roles shouldContainAll appUser.roles
        }
    }

    private fun givenDatabaseIsEmpty() {
        usersRepository.deleteAll()
    }

    private fun givenPasswordIsEncoded() {
        Mockito.`when`(passwordEncoder.encode("secret")).thenReturn("ENCODED")
    }
}