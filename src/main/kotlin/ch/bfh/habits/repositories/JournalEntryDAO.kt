package ch.bfh.habits.repositories

import ch.bfh.habits.entities.JournalEntry
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@RepositoryRestResource(collectionResourceRel = "journal_entries", path = "journal_entries")
@Repository
interface JournalEntryDAO : PagingAndSortingRepository<JournalEntry, Long> {
    fun findAllByHabitIdAndUserId(habitId: Long, userId: Long): List<JournalEntry>
    fun findAllByUserId(userId: Long): List<JournalEntry>
    fun findByUserIdAndId(userId: Long, id: Long): JournalEntry?
}
