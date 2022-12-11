package ch.bfh.habits.services

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.repositories.JournalEntryDAO
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

internal class JournalEntryServiceTest {
    @Test
    fun `newJournalEntry should link habit if habit is present`() {
        // given
        val habit = Habit("Test", "Test", 1, userId = 1)
        val journalEntry = JournalEntry("Test", habit, 1)

        val habitService = mockk<HabitService> {
            every { getHabit(any(), any()) } returns habit
        }
        val journalEntryDAO = mockk<JournalEntryDAO> {
            every { save(any()) } returns journalEntry
        }
        val journalEntryService = JournalEntryService(journalEntryDAO, habitService)

        val journalEntryDTO = JournalEntryDTO(
            description = "Test",
            habitId = 1,
        )
        val userId = 1L

        // when
        val newJournalEntry = journalEntryService.newJournalEntry(journalEntryDTO, userId)

        // then
        assertEquals(newJournalEntry.description, journalEntry.description)
        assertEquals(newJournalEntry.habit, journalEntry.habit)
    }

    @Test
    fun `updateJournalEntry should update habit link if habit is different`() {
        // given
        val habit = Habit("Test", "Test", 1, userId = 1)
        val habit2 = Habit("Test2", "Test2", 2, userId = 1)
        val journalEntry = JournalEntry("Test", habit, 1)

        val habitService = mockk<HabitService> {
            every { getHabit(1, any()) } returns habit
            every { getHabit(2, any()) } returns habit2
        }
        val journalEntryDAO = mockk<JournalEntryDAO> {
            every { findByUserIdAndId(any(), any()) } returns journalEntry
        }
        val journalEntryService = JournalEntryService(journalEntryDAO, habitService)

        val journalEntryDTO = JournalEntryDTO(
            description = "New Test",
            habitId = 2,
        )
        val userId = 1L

        // when
        val updatedJournalEntry = journalEntryService.updateJournalEntry(1, journalEntryDTO, userId)

        // then
        assertEquals(updatedJournalEntry.description, journalEntryDTO.description)
        assertEquals(updatedJournalEntry.habit, habit2)
    }

    @Test
    fun `updateJournalEntry should remove habit link if habit is null`() {
        // given
        val habit = Habit("Test", "Test", 1, userId = 1)
        val journalEntry = JournalEntry("Test", habit, 1)

        val habitService = mockk<HabitService> {
            every { getHabit(1, any()) } returns habit
        }
        val journalEntryDAO = mockk<JournalEntryDAO> {
            every { findByUserIdAndId(any(), any()) } returns journalEntry
        }
        val journalEntryService = JournalEntryService(journalEntryDAO, habitService)

        val journalEntryDTO = JournalEntryDTO(
            description = "New Test",
            habitId = null,
        )
        val userId = 1L

        // when
        val updatedJournalEntry = journalEntryService.updateJournalEntry(1, journalEntryDTO, userId)

        // then
        assertEquals(updatedJournalEntry.description, journalEntryDTO.description)
        assertEquals(updatedJournalEntry.habit, null)
    }

    @Test
    fun `updateJournalEntry should not update habit link if habit is the same`() {
        // given
        val habit = Habit("Test", "Test", 1, userId = 1)
        val journalEntry = JournalEntry("Test", habit, 1)

        val habitService = mockk<HabitService> {
            every { getHabit(any(), any()) } returns habit
        }
        val journalEntryDAO = mockk<JournalEntryDAO> {
            every { findByUserIdAndId(any(), any()) } returns journalEntry
        }
        val journalEntryService = JournalEntryService(journalEntryDAO, habitService)

        val journalEntryDTO = JournalEntryDTO(
            description = "New Test",
            habitId = 1,
        )
        val userId = 1L

        // when
        val updatedJournalEntry = journalEntryService.updateJournalEntry(1, journalEntryDTO, userId)

        // then
        assertEquals(updatedJournalEntry.description, journalEntryDTO.description)
        assertEquals(updatedJournalEntry.habit, habit)
        verify { habitService wasNot Called }
    }
}
