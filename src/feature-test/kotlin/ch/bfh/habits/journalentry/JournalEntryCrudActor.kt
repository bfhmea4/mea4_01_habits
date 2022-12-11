package ch.bfh.habits.journalentry

import ch.bfh.habits.auth.AuthCrudActor
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.entities.JournalEntry

interface JournalEntryCrudActor : AuthCrudActor{
    fun getsAllJournalEntriesForHabit(habitId: Long): List<JournalEntry>
    fun getsAllJournalEntries(): List<JournalEntry>
    fun createsJournalEntry(journalEntryDTO: JournalEntryDTO): JournalEntry
    fun seesJournalEntryExists(journalEntryId: Long): Boolean
    fun getsJournalEntry(journalEntryId: Long): JournalEntry
    fun deletesJournalEntry(journalEntryId: Long)
    fun updatesJournalEntry(journalEntryId: Long, journalEntryDTO: JournalEntryDTO): JournalEntry
}
