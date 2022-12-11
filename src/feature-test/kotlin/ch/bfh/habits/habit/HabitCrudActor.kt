package ch.bfh.habits.habit

import ch.bfh.habits.base.BaseCrudActor
import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.entities.Habit

interface HabitCrudActor : BaseCrudActor {
    fun getsAllHabits(): List<Habit>
    fun createsHabit(habitDTO: HabitDTO): Habit
    fun seesHabitExists(habitId: Long): Boolean
    fun getsHabit(habitId: Long): Habit
    fun deletesHabit(habitId: Long)
    fun updatesHabit(habitId: Long, habitDTO: HabitDTO): Habit
}
