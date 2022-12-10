package ch.bfh.habits.dtos.journalentry

import ch.bfh.habits.entities.JournalEntry

class JournalEntryEntityBuilder private constructor() {

    companion object {
        fun createJournalEntryEntityFromDTO(journalEntryDTO: JournalEntryDTO, userId: Long): JournalEntry {
            return JournalEntry(
                description = journalEntryDTO.description ?: "",
                userId = userId
            )
        }

        fun applyJournalEntryDtoToEntity(journalEntryDTO: JournalEntryDTO, journalEntryEntity: JournalEntry) {
            journalEntryEntity.description = journalEntryDTO.description ?: journalEntryEntity.description
        }
    }

}
