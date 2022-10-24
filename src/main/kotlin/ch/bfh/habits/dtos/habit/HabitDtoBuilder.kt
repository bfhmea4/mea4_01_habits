package ch.bfh.habits.dtos.habit

import ch.bfh.habits.entities.Habit

class HabitDtoBuilder private constructor() {

    companion object {
        fun createHabitDtoFromEntity(habitEntity: Habit): HabitDTO {
            return HabitDTO(
                id = habitEntity.id,
                title = habitEntity.title,
                description = habitEntity.description,
                completed = habitEntity.completed
            )
        }
    }
}