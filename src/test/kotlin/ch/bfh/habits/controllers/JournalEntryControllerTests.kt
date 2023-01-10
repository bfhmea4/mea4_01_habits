package ch.bfh.habits.controllers

import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.services.JournalEntryService
import ch.bfh.habits.services.UserService
import ch.bfh.habits.util.TokenProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals

@WebMvcTest(JournalEntryController::class)
internal class JournalEntryControllerTests {

    @TestConfiguration
    class HabitControllerTestsConfig {
        @Bean
        fun service() = mockk<JournalEntryService>()
        @Bean
        fun tokenProvider() = mockk<TokenProvider> {
            every { validateToken(any(), any()) } returns true
            every { extractUsername("correctUser") } returns "correctUser"
            every { extractUsername("wrongUser") } returns "wrongUser"
            every { extractId("correctUser") } returns 1
            every { extractId("wrongUser") } returns 2
        }
        @Bean
        fun userService() = mockk<UserService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: JournalEntryService

    private val mapper = jacksonObjectMapper()

    @Test
    @WithMockUser(username = "correctUser")
    fun `getAllJournalEntries return the current users journal entries`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        val journalEntry = JournalEntry("Done", habit, 1)
        every { service.getAllJournalEntries(1) } returns arrayListOf(journalEntry)
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        val result = mockMvc.perform(get("/api/journal_entries")
            .header("Authorization", userName))
            .andExpect(status().isOk).andReturn()

        // then
        assertEquals(mapper.writeValueAsString(arrayListOf(journalEntry)), result.response.contentAsString)
        verify { service.getAllJournalEntries(1) }
    }

    @Test
    @WithMockUser(username = "correctUser")
    fun `getJournalEntry returns the requested journal entry`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        val journalEntry = JournalEntry("Done", habit, 1)
        every { service.getJournalEntry(1, 1) } returns journalEntry
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        val result = mockMvc.perform(get("/api/journal_entry/1")
            .header("Authorization", userName))
            .andExpect(status().isOk).andReturn()

        // then
        assertEquals(mapper.writeValueAsString(journalEntry), result.response.contentAsString)
        verify { service.getJournalEntry(1, 1) }
    }

    @Test
    @WithMockUser(username = "wrongUser")
    fun `getJournalEntry returns not found if user is not allowed to access journal entry`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        val journalEntry = JournalEntry("Done", habit, 1)
        every { service.getJournalEntry(1, 1) } returns journalEntry
        every { service.getJournalEntry(1, 2) } throws  EntityNotFoundException("")
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        mockMvc.perform(get("/api/journal_entry/1")
            .header("Authorization", userName))
            .andExpect(status().isNotFound).andReturn()

        // then
        verify { service.getJournalEntry(1, 2) }
    }

    @Test
    @WithMockUser(username = "correctUser")
    fun `getJournalEntry returns not found if habit does not exist`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        val journalEntry = JournalEntry("Done", habit, 1)
        every { service.getJournalEntry(1, 1) } returns journalEntry
        every { service.getJournalEntry(2, 1) } throws  EntityNotFoundException("")
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        mockMvc.perform(get("/api/journal_entry/2")
            .header("Authorization", userName))
            .andExpect(status().isNotFound).andReturn()

        // then
        verify { service.getJournalEntry(2, 1) }
    }
}
