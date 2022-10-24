package ch.bfh.habits.habit

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO

interface HabitCrudActor {
    fun getAllHabits(): HabitListDTO
    fun newHabit(habitDTO: HabitDTO): Long
    fun seesHabitExists(habitId: Long): Boolean
    fun getsHabit(habitId: Long): HabitDTO
}
