package ch.bfh.habits.journalentry

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO

interface JournalEntryCrudActor {
    fun getsAllJournalEntriesForHabit(habitId: Long): JournalEntryListDTO
    fun getsAllJournalEntries(): JournalEntryListDTO
    fun createsJournalEntry(journalEntryDTO: JournalEntryDTO): Long
    fun seesJournalEntryExists(journalEntryId: Long): Boolean
    fun getsJournalEntry(journalEntryId: Long): JournalEntryDTO
    fun deletesJournalEntry(journalEntryId: Long)
    fun updatesJournalEntry(journalEntryId: Long, journalEntryDTO: JournalEntryDTO)
}
