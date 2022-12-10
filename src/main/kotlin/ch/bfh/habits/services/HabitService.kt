package ch.bfh.habits.services

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.habit.*
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.BadRequestException
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
        if (habitDTO.id != null) {
            throw BadRequestException("Id must not be set")
        }
        if ((habitDTO.frequencyValue != null && habitDTO.frequency == null) || (habitDTO.frequencyValue == null && habitDTO.frequency != null)) {
            throw BadRequestException("Frequency and frequencyValue must be set together")
        }
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

    @Transactional
    fun updateHabit(id: Long, habitDTO: HabitDTO, userId: Long) {
        if ((habitDTO.frequencyValue != null && habitDTO.frequency == null) || (habitDTO.frequencyValue == null && habitDTO.frequency != null)) {
            throw BadRequestException("Frequency and frequencyValue must be set together")
        }
        val currentHabit = habitDAO.findById(id).orElseThrow {
            EntityNotFoundException("Habit id = $id not found")
        }
        HabitEntityBuilder.applyHabitDtoToEntity(habitDTO, currentHabit)
    }

    private fun createHabitDtoFromEntity(habit: Habit) =
        HabitDtoBuilder.createHabitDtoFromEntity(habit)

    private fun createHabitEntityFromDTO(habitDTO: HabitDTO): Habit =
        HabitEntityBuilder.createHabitEntityFromDTO(habitDTO)
}
