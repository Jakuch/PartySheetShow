package com.jakuch.PartySheetShow.functionalTests.users

import com.jakuch.PartySheetShow.TestsBase
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class UserControllerFunctionalTests : TestsBase() {

    @Test
    fun `should return home page`() {
        //given

        //when then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(view().name("home"))
    }

    @Test
    fun `should return login page`() {
        //given

        //when then
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk)
                .andExpect(view().name("login"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `should return admin users page`() {
        //given

        //when then
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk)
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("users"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should return forbidden when user does not have ADMIN role`() {
        //given

        //when then
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden)
    }

    @Test
    fun `should redirect to login if no user specified`() {
        //given

        //when then
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `should delete user`() {
        //given
        givenUserPresentInDatabase(givenUser(id = "1"))

        //when
        mockMvc.perform(post("/admin/users")
                .with(csrf())
                .param("delete", "")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/users"))

        //then
        usersRepository.findAll().size shouldBe 0
    }

    @Test
    @WithMockUser(authorities = ["ROLE_PLAYER"])
    fun `should not allow deleting user without ADMIN role`() {
        //given
        givenUserPresentInDatabase(givenUser(id = "1"))

        //when
        mockMvc.perform(post("/admin/users")
                .with(csrf())
                .param("delete", "")
                .param("id", "1"))
                .andExpect(status().isForbidden)

        //then
        usersRepository.findAll().size shouldBe 1
    }
}