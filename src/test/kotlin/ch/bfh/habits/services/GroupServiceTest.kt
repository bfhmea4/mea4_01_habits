package ch.bfh.habits.services

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.entities.Group
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.repositories.HabitDAO
import ch.bfh.habits.repositories.JournalEntryDAO
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class GroupServiceTest {
    @Test
    fun `newHabit should link group if group is present`() {
        // given
        val group = Group("Test", 1, userId = 1)
        val habit = Habit("Test", "Test", 1, userId = 1, group = group)

        val groupService = mockk<GroupService> {
            every { getGroup(any(), any()) } returns group
        }

        val habitDAO = mockk<HabitDAO> {
            every { save(any()) } returns habit
        }
        val habitService = HabitService(habitDAO, groupService)

        val habitDTO = HabitDTO(
            title = "Test",
            groupId = 1,
        )
        val userId = 1L

        // when
        val newHabit = habitService.newHabit(habitDTO, userId)

        // then
        assertEquals(newHabit.title, habit.title)
        assertEquals(newHabit.group, habit.group)
    }

    @Test
    fun `updateHabit should update group link if group is different`() {
        // given
        val group = Group("Private", 1, userId = 1)
        val group2 = Group("BFH", 1, userId = 1)
        val habit = Habit("Test", "Test", 1, userId = 1, group = group)

        val groupService = mockk<GroupService> {
            every { getGroup(1, any()) } returns group
            every { getGroup(2, any()) } returns group2
        }

        val habitDAO = mockk<HabitDAO> {
            every { save(any()) } returns habit
            every { findByUserIdAndId(any(), any()) } returns habit
        }
        val habitService = HabitService(habitDAO, groupService)

        val habitDTO = HabitDTO(
            title = "Test",
            groupId = 2,
        )
        val userId = 1L

        // when
        val updateHabit = habitService.updateHabit(1, habitDTO, userId)

        // then
        assertEquals(updateHabit.title, habitDTO.title)
        assertEquals(updateHabit.group, group2)
    }

    @Test
    fun `updateHabit should remove group link if group is null`() {
        val group = Group("Private", 1, userId = 1)
        val habit = Habit("Test", "Test", 1, userId = 1, group = group)

        val groupService = mockk<GroupService> {
            every { getGroup(any(), any()) } returns group
        }

        val habitDAO = mockk<HabitDAO> {
            every { save(any()) } returns habit
            every { findByUserIdAndId(any(), any()) } returns habit
        }
        val habitService = HabitService(habitDAO, groupService)

        val habitDTO = HabitDTO(
            title = "Test",
            groupId = null,
        )
        val userId = 1L

        // when
        val updateHabit = habitService.updateHabit(1, habitDTO, userId)

        // then
        assertEquals(updateHabit.title, habitDTO.title)
        assertEquals(updateHabit.group, null)
    }

    @Test
    fun `updateHabit should not update group link if group is the same`() {
        // given
        val group = Group("Private", 1, userId = 1)
        val habit = Habit("Test", "Test", 1, userId = 1, group = group)

        val groupService = mockk<GroupService> {
            every { getGroup(any(), any()) } returns group
        }

        val habitDAO = mockk<HabitDAO> {
            every { save(any()) } returns habit
            every { findByUserIdAndId(any(), any()) } returns habit
        }
        val habitService = HabitService(habitDAO, groupService)

        val habitDTO = HabitDTO(
            title = "Test",
            groupId = 1,
        )
        val userId = 1L

        // when
        val updateHabit = habitService.updateHabit(1, habitDTO, userId)

        // then
        assertEquals(updateHabit.title, habit.title)
        assertEquals(updateHabit.group, group)
        verify { groupService wasNot Called }
    }
}
