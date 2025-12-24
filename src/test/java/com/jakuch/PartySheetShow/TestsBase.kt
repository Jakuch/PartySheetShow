package com.jakuch.PartySheetShow

import com.jakuch.PartySheetShow.initiativeTracker.repository.InitiativeTrackerRepository
import com.jakuch.PartySheetShow.player.character.repository.CharacterRepository
import com.jakuch.PartySheetShow.security.model.AppUser
import com.jakuch.PartySheetShow.security.model.UserRole
import com.jakuch.PartySheetShow.security.repository.UsersRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc


@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestsConfig::class)
@SpringBootTest(classes = [Main::class])
abstract class TestsBase {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @MockBean
    protected lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    protected lateinit var usersRepository: UsersRepository

    @Autowired
    protected lateinit var initiativeTrackerRepository: InitiativeTrackerRepository

    @Autowired
    protected lateinit var characterRepository: CharacterRepository

    @AfterEach
    fun cleanupDatabase() {
        usersRepository.deleteAll()
        initiativeTrackerRepository.deleteAll()
        characterRepository.deleteAll()
    }

    protected fun givenDatabaseIsEmpty() {
        usersRepository.deleteAll()
    }

    protected fun givenUserPresentInDatabase(user: AppUser) {
        usersRepository.save(user)
    }

    protected fun givenUser(id: String? = null, username: String = "username", password: String = "secret", roles: List<UserRole> = listOf(UserRole.PLAYER, UserRole.DM)): AppUser {
        return AppUser().apply {
            this.id = id
            this.username = username
            this.password = password
            this.roles = roles
        }
    }
}