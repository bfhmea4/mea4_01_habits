package ch.bfh.habits.services

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDtoBuilder
import ch.bfh.habits.dtos.journalentry.JournalEntryEntityBuilder
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.HabitDAO
import ch.bfh.habits.repositories.JournalEntryDAO
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JournalEntryService(private val journalEntryDAO: JournalEntryDAO, private val habitDAO: HabitDAO) {
    @Transactional
    fun getAllJournalEntriesForHabit(id: Long): JournalEntryListDTO {
        val journalEntries =  ArrayList<JournalEntryDTO>()
        journalEntryDAO.findAll().forEach { i ->
            if (i.belongsTo?.id == id) {
                journalEntries.add(createJournalEntryDtoFromEntity(i))
            }
        }
        return JournalEntryListDTO(journalEntries = journalEntries)
    }

    @Transactional
    fun getAllJournalEntries(): JournalEntryListDTO {
        val journalEntries =  ArrayList<JournalEntryDTO>()
        journalEntryDAO.findAll().forEach { i -> journalEntries.add(createJournalEntryDtoFromEntity(i))}
        return JournalEntryListDTO(journalEntries = journalEntries)
    }

    @Transactional
    fun newJournalEntry(journalEntryDTO: JournalEntryDTO): ObjectIdDTO {
        val newJournalEntry = createJournalEntryEntityFromDTO(journalEntryDTO)

        // ToDO Do we probably need two DTOs. One as input and one as output
        // Is there a better way. Using the HabitService somehow
        if (journalEntryDTO.belongsToId != null) {
            val habit = habitDAO.findById(journalEntryDTO.belongsToId!!).orElseThrow {
                EntityNotFoundException("Habit id = ${journalEntryDTO.belongsToId!!} not found")
            }
            newJournalEntry.belongsTo = habit
        }

        journalEntryDAO.save(newJournalEntry)
        return ObjectIdDTO(newJournalEntry.id!!)
    }

    @Transactional
    fun getJournalEntryById(id: Long): JournalEntryDTO {
        val journalEntry = journalEntryDAO.findById(id).orElseThrow {
            EntityNotFoundException("Journal Entry id = $id not found")
        }
        return createJournalEntryDtoFromEntity(journalEntry)
    }

    @Transactional
    fun deleteJournalEntryById(id: Long) {
        try {
            journalEntryDAO.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("Journal Entry id = $id not found")
        }
    }

    @Transactional
    fun updateJournalEntryById(id: Long, journalEntryDTO: JournalEntryDTO) {
        val currentJournalEntry = journalEntryDAO.findById(id).orElseThrow {
            EntityNotFoundException("Journal Entry id = $id not found")
        }

        if (currentJournalEntry.belongsTo?.id != journalEntryDTO.belongsToId) {
            val habitId = if (journalEntryDTO.belongsToId != null) journalEntryDTO.belongsToId else null

            if (habitId != null) {
                val habit = habitDAO.findById(habitId).orElseThrow {
                    EntityNotFoundException("Habit id = $habitId not found")
                }
                currentJournalEntry.belongsTo = habit
            } else {
                currentJournalEntry.belongsTo = null
            }
        }

        JournalEntryEntityBuilder.applyJournalEntryDtoToEntity(journalEntryDTO, currentJournalEntry)
        journalEntryDAO.save(currentJournalEntry)
    }

    private fun createJournalEntryDtoFromEntity(journalEntry: JournalEntry) =
        JournalEntryDtoBuilder.createJournalEntryDtoFromEntity(journalEntry)

    private fun createJournalEntryEntityFromDTO(journalEntryDTO: JournalEntryDTO): JournalEntry =
        JournalEntryEntityBuilder.createJournalEntryEntityFromDTO(journalEntryDTO)
}
