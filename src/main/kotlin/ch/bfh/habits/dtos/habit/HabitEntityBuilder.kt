package ch.bfh.habits.dtos.habit

import ch.bfh.habits.entities.Habit

class HabitEntityBuilder private constructor() {

    companion object {
        fun createHabitEntityFromDTO(habitDTO: HabitDTO): Habit {
            return Habit(
                title = habitDTO.title ?: "",
                description = habitDTO.description ?: "",
                frequency = habitDTO.frequency,
                frequencyValue = habitDTO.frequencyValue
            )
        }

        fun applyHabitDtoToEntity(habitDTO: HabitDTO, habitEntity: Habit) {
            habitEntity.title = habitDTO.title ?: habitEntity.title
            habitEntity.description = habitDTO.description ?: habitEntity.description
            habitEntity.frequency = habitDTO.frequency
            habitEntity.frequencyValue = habitDTO.frequencyValue
        }
    }

}
