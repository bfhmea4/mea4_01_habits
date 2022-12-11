package ch.bfh.habits.services

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
    fun getAllHabits(userId: Long): List<Habit> {
        return habitDAO.findAllByUserId(userId)
    }

    @Transactional
    fun newHabit(habitDTO: HabitDTO, userId: Long): Habit {
        if ((habitDTO.frequencyValue != null && habitDTO.frequency == null) || (habitDTO.frequencyValue == null && habitDTO.frequency != null)) {
            throw BadRequestException("Frequency and frequencyValue must be set together")
        }
        val newHabit = HabitEntityBuilder.createHabitEntityFromDTO(habitDTO, userId)
        return habitDAO.save(newHabit)
    }

    @Transactional
    fun getHabit(id: Long, userId: Long): Habit {
        return habitDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Habit not found or not owned by user")
    }

    @Transactional
    fun deleteHabit(id: Long, userId: Long) {
        return habitDAO.delete(getHabit(id, userId))
    }

    @Transactional
    fun updateHabit(id: Long, habitDTO: HabitDTO, userId: Long): Habit {
        if ((habitDTO.frequencyValue != null && habitDTO.frequency == null) || (habitDTO.frequencyValue == null && habitDTO.frequency != null)) {
            throw BadRequestException("Frequency and frequencyValue must be set together")
        }
        val currentHabit = habitDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Habit not found or not owned by user")
        HabitEntityBuilder.applyHabitDtoToEntity(habitDTO, currentHabit)
        return currentHabit
    }
}
