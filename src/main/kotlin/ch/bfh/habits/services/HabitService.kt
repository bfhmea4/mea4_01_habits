package ch.bfh.habits.services

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitDtoBuilder
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.repositories.HabitDAO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HabitService(private val habitDAO: HabitDAO) {
    @Transactional
    fun getAllHabits(): HabitListDTO {
        val habits =  ArrayList<HabitDTO>()
        habitDAO.findAll().forEach { i -> habits.add(createHabitDtoFromEntity(i))}
        return HabitListDTO(habits = habits)
    }

    private fun createHabitDtoFromEntity(habit: Habit) =
        HabitDtoBuilder.createHabitDtoFromEntity(habit)
}
