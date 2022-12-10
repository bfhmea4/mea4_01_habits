package ch.bfh.habits.dtos.journalentry

data class JournalEntryDTO(
    val description: String? = null,
    var habitId: Long? = null,
)
