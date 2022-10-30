package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.services.JournalEntryService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@ExtendWith(SpringExtension::class)
@WebMvcTest(JournalEntryController::class)
internal class JournalEntryControllerTests {

    @TestConfiguration
    class JournalEntryControllerTestsConfig {
        @Bean
        fun service() = mockk<JournalEntryService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: JournalEntryService

    private val mapper = jacksonObjectMapper()

    @Test
    fun controller_invokes_getAllJournalEntriesForHabit_function() {
        val habit = Habit("Gym",1)
        val journalEntry = JournalEntryDTO("Done", habit = habit)

        // ToDo test service layer also. This logic is not tested here
        every { service.getAllJournalEntriesForHabit(1) } returns JournalEntryListDTO(arrayListOf(journalEntry))

        val result = mockMvc.perform(get("/api/habit/1/journal_entries"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(
            mapper.writeValueAsString(JournalEntryListDTO(arrayListOf(journalEntry))),
            result.response.contentAsString
        )
        verify { service.getAllJournalEntriesForHabit(1) }
    }

    @Test
    fun controller_invokes_getAllJournalEntries_function() {
        val habit = Habit("Gym", 1)
        val journalEntry = JournalEntryDTO("Done", habit = habit)

        every { service.getAllJournalEntries() } returns JournalEntryListDTO(arrayListOf(journalEntry))

        val result = mockMvc.perform(get("/api/journal_entries"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(
            mapper.writeValueAsString(JournalEntryListDTO(arrayListOf(journalEntry))),
            result.response.contentAsString
        )
        verify { service.getAllJournalEntries() }
    }

    @Test
    fun controller_invokes_newJournalEntry_function() {
        val journalEntry = JournalEntryDTO("Done", habitId = 1)

        every { service.newJournalEntry(any()) } returns ObjectIdDTO(1)

        val result = mockMvc.perform(post("/api/journal_entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(journalEntry)))
            .andExpect(status().isCreated)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(ObjectIdDTO(1)), result.response.contentAsString)
        verify { service.newJournalEntry(any()) }
    }

    @Test
    fun controller_invokes_getJournalEntry_function() {
        val habit = Habit("Gym", 1)
        val journalEntry = JournalEntryDTO("Done", habit = habit, id = 1)

        every { service.getJournalEntryById(1) } returns journalEntry

        val result = mockMvc.perform(get("/api/journal_entry/1"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(journalEntry), result.response.contentAsString)
        verify { service.getJournalEntryById(1) }
    }

    @Test
    fun controller_invokes_deleteJournalEntry_function() {
        every { service.deleteJournalEntryById(1) } returns Unit

        mockMvc.perform(delete("/api/journal_entry/1"))
            .andExpect(status().isNoContent)

        verify { service.deleteJournalEntryById(1) }
    }

    @Test
    fun controller_invokes_deleteJournalEntry_function_on_non_existent_id() {
        every { service.deleteJournalEntryById(2) } throws EntityNotFoundException("Journal Entry not found")

        mockMvc.perform(delete("/api/journal_entry/2"))
            .andExpect(status().is4xxClientError)

        verify { service.deleteJournalEntryById(2) }
    }

    @Test
    fun controller_invokes_updateJournalEntry_function() {
        val habit = Habit("Gym", 1)
        val journalEntry = JournalEntryDTO("Done_Old", habitId = 1)
        val journalEntryReturned = JournalEntryDTO("Done_Old", habit = habit, id = 1)

        every { service.updateJournalEntryById(1, any()) } returns Unit
        every { service.getJournalEntryById(1) } returns journalEntryReturned

        mockMvc.perform(put("/api/journal_entry/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(journalEntry)))
            .andExpect(status().isOk)

        val result = mockMvc.perform(get("/api/journal_entry/1"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(journalEntryReturned), result.response.contentAsString)
        verify { service.updateJournalEntryById(1, any()) }
        verify { service.getJournalEntryById(1) }
    }
}
