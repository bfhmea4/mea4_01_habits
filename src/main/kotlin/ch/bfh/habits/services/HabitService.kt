package ch.bfh.habits.services

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitDtoBuilder
import ch.bfh.habits.dtos.habit.HabitEntityBuilder
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.HabitDAO
import org.springframework.dao.EmptyResultDataAccessException
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

    @Transactional
    fun newHabit(habitDTO: HabitDTO): ObjectIdDTO {
        val newHabit = createHabitEntityFromDTO(habitDTO)
        habitDAO.save(newHabit)
        return ObjectIdDTO(newHabit.id!!)
    }

    @Transactional
    fun getHabitById(id: Long): HabitDTO {
        val habit = habitDAO.findById(id).orElseThrow {
            EntityNotFoundException("Habit id = $id not found")
        }
        return createHabitDtoFromEntity(habit)
    }

    @Transactional
    fun deleteHabitById(id: Long) {
        try {
            habitDAO.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("Habit id = $id not found")
        }
    }

    private fun createHabitDtoFromEntity(habit: Habit) =
        HabitDtoBuilder.createHabitDtoFromEntity(habit)

    private fun createHabitEntityFromDTO(habitDTO: HabitDTO): Habit =
        HabitEntityBuilder.createHabitEntityFromDTO(habitDTO)
}
