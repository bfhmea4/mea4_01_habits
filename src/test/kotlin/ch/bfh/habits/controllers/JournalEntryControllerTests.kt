package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.User
import ch.bfh.habits.services.JournalEntryService
import ch.bfh.habits.services.UserService
import ch.bfh.habits.util.TokenProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals
import org.springframework.security.core.userdetails.User as SpringUser

@ExtendWith(SpringExtension::class)
@WebMvcTest(JournalEntryController::class)
@Disabled // ToDo remove
internal class JournalEntryControllerTests {

    @TestConfiguration
    class JournalEntryControllerTestsConfig {
        @Bean
        fun service() = mockk<JournalEntryService>()

        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun tokenProvider() = mockk<TokenProvider>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: JournalEntryService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    private val mapper = jacksonObjectMapper()

    // Still doesn't work
    @Test
    fun logged_in_user_can_see_entries() {
        val user = User("Test", "Test", "Test", "Test", 1L)
        val userDetails = SpringUser("Test", "Test", emptyList())
        val token = "Bearer token"

        val habit = Habit("Gym", "Go to the gym",1, userId = user.id!!)
        val journalEntry = JournalEntryDTO("Done", habit.id, habit, user.id)


        every { userService.loadUserByUsername("testUser") } returns userDetails
        every { tokenProvider.validateToken(token, userDetails) } returns true
        every { service.getAllJournalEntries(1) } returns JournalEntryListDTO(arrayListOf(journalEntry))

        val result = mockMvc.perform(get("/api/journalEntries")
            .header("Authorization", token)).andExpect(status().isOk).andReturn()

        assertEquals(mapper.writeValueAsString(JournalEntryListDTO(arrayListOf(journalEntry))), result.response.contentAsString)
    }
}
