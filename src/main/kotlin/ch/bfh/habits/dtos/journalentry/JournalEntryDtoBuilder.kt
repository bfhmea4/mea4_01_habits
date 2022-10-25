package ch.bfh.habits.dtos.journalentry

import ch.bfh.habits.entities.JournalEntry

class JournalEntryDtoBuilder private constructor() {

    companion object {
        fun createJournalEntryDtoFromEntity(journalEntryEntity: JournalEntry): JournalEntryDTO {
            return JournalEntryDTO(
                id = journalEntryEntity.id,
                description = journalEntryEntity.description,
                belongsTo = journalEntryEntity.belongsTo,
                createdAt = journalEntryEntity.createdAt,
                editedAt = journalEntryEntity.editedAt
            )
        }
    }
}
