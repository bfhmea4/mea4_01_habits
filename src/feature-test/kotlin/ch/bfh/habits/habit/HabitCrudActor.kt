package ch.bfh.habits.habit

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO

interface HabitCrudActor {
    fun getsAllHabits(): HabitListDTO
    fun createsHabit(habitDTO: HabitDTO): Long
    fun seesHabitExists(habitId: Long): Boolean
    fun getsHabit(habitId: Long): HabitDTO
    fun deletesHabit(habitId: Long)
    fun updatesHabit(habitId: Long, habitDTO: HabitDTO)
}
