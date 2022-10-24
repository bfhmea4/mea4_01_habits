package ch.bfh.habits.dtos.habit

data class HabitDTO(
    val title: String? = null,
    val description: String? = null,
    val completed: Boolean? = null,
    val id: Long? = null,
)
