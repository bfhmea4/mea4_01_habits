package ch.bfh.habits.dtos.journalentry

import ch.bfh.habits.entities.Habit
import java.sql.Timestamp

data class JournalEntryDTO(
    val description: String? = null,
    var habitId: Long? = null,
    var habit: Habit? = null,
    val id: Long? = null,
    val createdAt: Timestamp? = null,
    val editedAt: Timestamp? = null
)
