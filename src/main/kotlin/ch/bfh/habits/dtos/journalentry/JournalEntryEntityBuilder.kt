package ch.bfh.habits.dtos.journalentry

import ch.bfh.habits.entities.JournalEntry

class JournalEntryEntityBuilder private constructor() {

    companion object {
        fun createJournalEntryEntityFromDTO(journalEntryDTO: JournalEntryDTO): JournalEntry {
            return JournalEntry(
                description = journalEntryDTO.description ?: ""
            )
        }

        fun applyJournalEntryDtoToEntity(journalEntryDTO: JournalEntryDTO, journalEntryEntity: JournalEntry) {
            journalEntryEntity.description = journalEntryDTO.description ?: journalEntryEntity.description
        }
    }

}
