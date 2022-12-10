package ch.bfh.habits.services

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDtoBuilder
import ch.bfh.habits.dtos.journalentry.JournalEntryEntityBuilder
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.HabitDAO
import ch.bfh.habits.repositories.JournalEntryDAO
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JournalEntryService(private val journalEntryDAO: JournalEntryDAO, private val habitDAO: HabitDAO) {
    @Transactional
    fun getAllJournalEntriesForHabit(id: Long, userId: Long): JournalEntryListDTO {
        val journalEntries =  ArrayList<JournalEntryDTO>()
        journalEntryDAO.findAllByHabitIdAndUserId(id, userId).forEach { i -> journalEntries.add(JournalEntryDtoBuilder.createJournalEntryDtoFromEntity(i))}
        return JournalEntryListDTO(journalEntries = journalEntries)
    }

    @Transactional
    fun getAllJournalEntries(userId: Long): JournalEntryListDTO {
        val journalEntries =  ArrayList<JournalEntryDTO>()
        journalEntryDAO.findAllByUserId(userId).forEach { i -> journalEntries.add(JournalEntryDtoBuilder.createJournalEntryDtoFromEntity(i))}
        return JournalEntryListDTO(journalEntries = journalEntries)
    }

    @Transactional
    fun newJournalEntry(journalEntryDTO: JournalEntryDTO, userId: Long): ObjectIdDTO {
        val newJournalEntry = JournalEntryEntityBuilder.createJournalEntryEntityFromDTO(journalEntryDTO, userId)

        // ToDO Do we probably need two DTOs. One as input and one as output
        // Is there a better way. Using the HabitService somehow
        if (journalEntryDTO.habitId != null) {
            val habit = habitDAO.findById(journalEntryDTO.habitId!!).orElseThrow {
                EntityNotFoundException("Habit id = ${journalEntryDTO.habitId!!} not found")
            }
            newJournalEntry.habit = habit
        }

        journalEntryDAO.save(newJournalEntry)
        return ObjectIdDTO(newJournalEntry.id!!)
    }

    @Transactional
    fun getJournalEntryById(id: Long, userId: Long): JournalEntryDTO {
        val journalEntry = journalEntryDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Journal Entry not found or not owned by user")
        return JournalEntryDtoBuilder.createJournalEntryDtoFromEntity(journalEntry)
    }

    @Transactional
    fun deleteJournalEntryById(id: Long, userId: Long) {
        try {
            journalEntryDAO.deleteByIdAndUserId(id, userId)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("Journal Entry not found or not owned by user")
        }
    }

    @Transactional
    fun updateJournalEntryById(id: Long, journalEntryDTO: JournalEntryDTO, userId: Long) {
        val currentJournalEntry = journalEntryDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Journal Entry not found or not owned by user")

        if (currentJournalEntry.habit?.id != journalEntryDTO.habitId) {
            val habitId = if (journalEntryDTO.habitId != null) journalEntryDTO.habitId else null

            if (habitId != null) {
                val habit = habitDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Habit not found or not owned by user")
                currentJournalEntry.habit = habit
            } else {
                currentJournalEntry.habit = null
            }
        }

        JournalEntryEntityBuilder.applyJournalEntryDtoToEntity(journalEntryDTO, currentJournalEntry)
    }
}
