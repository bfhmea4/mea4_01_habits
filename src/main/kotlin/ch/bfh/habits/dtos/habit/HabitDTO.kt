package ch.bfh.habits.dtos.habit

import ch.bfh.habits.entities.enums.Frequency

data class HabitDTO(
    val title: String? = null,
    val description: String? = null,
    val frequency: Frequency? = null,
    val frequencyValue: Long? = null
)
