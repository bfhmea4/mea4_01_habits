package ch.bfh.habits.dtos.habit

import java.sql.Timestamp

data class HabitDTO(
    val title: String? = null,
    val description: String? = null,
    val id: Long? = null,
    val createdAt: Timestamp? = null,
    val editedAt: Timestamp? = null,
    val frequency: Frequency? = null,
    val frequencyValue: Long? = null
)
