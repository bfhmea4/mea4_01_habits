package ch.bfh.habits.dtos.journalentry

import ch.bfh.habits.entities.Habit
import java.sql.Timestamp

data class JournalEntryDTO(
    val description: String? = null,
    var belongsToId: Long? = null,
    var belongsTo: Habit? = null,
    val id: Long? = null,
    val createdAt: Timestamp? = null,
    val editedAt: Timestamp? = null
)
