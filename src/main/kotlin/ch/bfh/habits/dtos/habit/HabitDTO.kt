package ch.bfh.habits.dtos.habit

data class HabitDTO(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val completed: Boolean? = null
)
