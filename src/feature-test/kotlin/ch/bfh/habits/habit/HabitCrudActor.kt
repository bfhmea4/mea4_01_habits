package ch.bfh.habits.habit

import ch.bfh.habits.auth.AuthCrudActor
import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.entities.Habit

interface HabitCrudActor : AuthCrudActor {
    fun getsAllHabits(): List<Habit>
    fun createsHabit(habitDTO: HabitDTO): Habit
    fun seesHabitExists(habitId: Long): Boolean
    fun getsHabit(habitId: Long): Habit
    fun deletesHabit(habitId: Long)
    fun updatesHabit(habitId: Long, habitDTO: HabitDTO): Habit
}
