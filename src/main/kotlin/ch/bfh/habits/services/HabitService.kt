package ch.bfh.habits.services

import ch.bfh.habits.dtos.habit.*
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.HabitDAO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HabitService(private val habitDAO: HabitDAO, private val groupService: GroupService) {
    @Transactional
    fun getAllHabitsForGroup(id: Long, userId: Long): List<Habit> {
        return habitDAO.findAllByGroupIdAndUserId(id, userId)
    }

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

        if (habitDTO.groupId != null) {
            val group = groupService.getGroup(habitDTO.groupId!!, userId)
            newHabit.group = group
        }

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

        if (currentHabit.group?.id != habitDTO.groupId) {
            val groupId = if (habitDTO.groupId != null) habitDTO.groupId else null

            if (groupId != null) {
                val group = groupService.getGroup(groupId, userId)
                currentHabit.group = group
            } else {
                currentHabit.group = null
            }
        }

        HabitEntityBuilder.applyHabitDtoToEntity(habitDTO, currentHabit)
        return currentHabit
    }
}
