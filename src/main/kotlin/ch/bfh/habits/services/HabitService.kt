package ch.bfh.habits.services

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.habit.*
import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.HabitDAO
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HabitService(private val habitDAO: HabitDAO) {
    @Transactional
    fun getAllHabits(userId: Long): HabitListDTO {
        val habits =  ArrayList<HabitDTO>()
        habitDAO.findAllByUserId(userId).forEach { i -> habits.add(HabitDtoBuilder.createHabitDtoFromEntity(i))}
        return HabitListDTO(habits = habits)
    }

    @Transactional
    fun newHabit(habitDTO: HabitDTO, userId: Long): ObjectIdDTO {
        if (habitDTO.id != null) {
            throw BadRequestException("Id must not be set")
        }
        if ((habitDTO.frequencyValue != null && habitDTO.frequency == null) || (habitDTO.frequencyValue == null && habitDTO.frequency != null)) {
            throw BadRequestException("Frequency and frequencyValue must be set together")
        }
        val newHabit = HabitEntityBuilder.createHabitEntityFromDTO(habitDTO, userId)
        habitDAO.save(newHabit)
        return ObjectIdDTO(newHabit.id!!)
    }

    @Transactional
    fun getHabit(id: Long, userId: Long): HabitDTO {
        val habit = habitDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Habit not found or not owned by user")
        return HabitDtoBuilder.createHabitDtoFromEntity(habit)
    }

    @Transactional
    fun deleteHabit(id: Long, userId: Long) {
        try {
            habitDAO.deleteByIdAndUserId(id, userId)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("Habit not found or not owned by user")
        }
    }

    @Transactional
    fun updateHabitById(id: Long, habitDTO: HabitDTO) {
        if ((habitDTO.frequencyValue != null && habitDTO.frequency == null) || (habitDTO.frequencyValue == null && habitDTO.frequency != null)) {
            throw BadRequestException("Frequency and frequencyValue must be set together")
        }
        val currentHabit = habitDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Habit not found or not owned by user")
        HabitEntityBuilder.applyHabitDtoToEntity(habitDTO, currentHabit)
    }
}
