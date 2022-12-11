package ch.bfh.habits.services

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.entities.enums.Frequency
import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.repositories.HabitDAO
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.assertThrows

internal class HabitServiceTest {
    private val habitDAO = mockk<HabitDAO>()
    private val habitService = HabitService(habitDAO)

    @Test
    fun `newHabit should throw BadRequestException if frequency and frequencyValue are not set together`() {
        val habitDTO = HabitDTO(
            title = "Test",
            description = "Test",
            frequency = null,
            frequencyValue = 1
        )
        val userId = 1L

        assertThrows<BadRequestException> {
            habitService.newHabit(habitDTO, userId)
        }
    }

    @Test
    fun `updateHabit should throw BadRequestException if frequency and frequencyValue are not set together`() {
        val habitDTO = HabitDTO(
            title = "Test",
            description = "Test",
            frequency = Frequency.DAILY,
            frequencyValue = null
        )
        val userId = 1L
        val id = 1L

        assertThrows<BadRequestException> {
            habitService.updateHabit(id, habitDTO, userId)
        }
    }
}
