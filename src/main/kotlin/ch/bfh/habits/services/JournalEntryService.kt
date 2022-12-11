package ch.bfh.habits.services

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryEntityBuilder
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.JournalEntryDAO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JournalEntryService(private val journalEntryDAO: JournalEntryDAO, private val habitService: HabitService) {
    @Transactional
    fun getAllJournalEntriesForHabit(id: Long, userId: Long): List<JournalEntry> {
        return journalEntryDAO.findAllByHabitIdAndUserId(id, userId)
    }

    @Transactional
    fun getAllJournalEntries(userId: Long): List<JournalEntry> {
        return journalEntryDAO.findAllByUserId(userId)
    }

    @Transactional
    fun newJournalEntry(journalEntryDTO: JournalEntryDTO, userId: Long): JournalEntry {
        val newJournalEntry = JournalEntryEntityBuilder.createJournalEntryEntityFromDTO(journalEntryDTO, userId)

        if (journalEntryDTO.habitId != null) {
            val habit = habitService.getHabit(journalEntryDTO.habitId!!, userId)
            newJournalEntry.habit = habit
        }

        return journalEntryDAO.save(newJournalEntry)
    }

    @Transactional
    fun getJournalEntry(id: Long, userId: Long): JournalEntry {
        return journalEntryDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Journal Entry not found or not owned by user")
    }

    @Transactional
    fun deleteJournalEntry(id: Long, userId: Long) {
        journalEntryDAO.delete(getJournalEntry(id, userId))
    }

    @Transactional
    fun updateJournalEntry(id: Long, journalEntryDTO: JournalEntryDTO, userId: Long): JournalEntry {
        val currentJournalEntry = journalEntryDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Journal Entry not found or not owned by user")

        if (currentJournalEntry.habit?.id != journalEntryDTO.habitId) {
            val habitId = if (journalEntryDTO.habitId != null) journalEntryDTO.habitId else null

            if (habitId != null) {
                val habit = habitService.getHabit(habitId, userId)
                currentJournalEntry.habit = habit
            } else {
                currentJournalEntry.habit = null
            }
        }

        JournalEntryEntityBuilder.applyJournalEntryDtoToEntity(journalEntryDTO, currentJournalEntry)
        return currentJournalEntry
    }
}
