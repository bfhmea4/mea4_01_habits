package ch.bfh.habits.habit

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Disabled // ToDo remove
class HabitCrudFeatureTests {

    @Autowired
    private lateinit var habitActor: HabitCrudActor

    @Nested
    @DisplayName("Given no habits exist THEN we")
    inner class GivenNoHabitExists {
        @Test
        fun `get an empty list for get all`() {
            assertThat(habitActor.getsAllHabits()).isEqualTo(arrayListOf<Habit>())
        }
    }

    @Nested
    @DisplayName("Given we have created a habit THEN we ...")
    inner class GivenNewHabitCreated {
        private var habit = habitActor.createsHabit(createHabitDTO())

        @Test
        fun `can find it amongst all habits`() {
            // when
            habitActor.createsHabit(createHabitDTO("Running", "Go for a run"))
            val allHabits = habitActor.getsAllHabits()

            // then
            assertThat(allHabits.size).isGreaterThan(1)
            assertThat(allHabits.filter { i -> i.id == habit.id }).isNotEmpty
        }

        @Test
        fun `can find it`() {
            assertThat(habitActor.seesHabitExists(habit.id!!)).isTrue
        }

        @Test
        fun `can get it`() {
            assertThat(habitActor.getsHabit(habit.id!!).id).isEqualTo(habit.id)
        }

        @Test
        fun `can delete it`() {
            habitActor.deletesHabit(habit.id!!)
            assertThat(habitActor.seesHabitExists(habit.id!!)).isFalse
        }

        @Test
        fun `can update it`() {
            // given
            val newHabit = createHabitDTO("New", "New")
            // when
            habitActor.updatesHabit(habit.id!!, newHabit)
            // then
            val updatedHabit = habitActor.getsHabit(habit.id!!)
            assertThat(updatedHabit.title).isEqualTo("New")
            assertThat(updatedHabit.description).isEqualTo("New")
        }
    }

    @Nested
    @DisplayName("Given a non-existing habit id THEN ...")
    inner class GivenNonExistingHabit {
        private val nonExistingHabitId = -1L

        @Test
        fun `find returns false`() {
            assertThat(habitActor.seesHabitExists(nonExistingHabitId)).isFalse
        }

        @Test
        fun `get throws not found`() {
            assertThrows<EntityNotFoundException> {
                habitActor.getsHabit(nonExistingHabitId)
            }
        }

        @Test
        fun `delete throws not found`() {
            assertThrows<EntityNotFoundException> {
                habitActor.deletesHabit(nonExistingHabitId)
            }
        }

        @Test
        fun `update throws bad request`() {
            assertThrows<BadRequestException> {
                habitActor.updatesHabit(nonExistingHabitId, createHabitDTO())
            }
        }

        @Test
        fun `update throws not found`() {
            assertThrows<EntityNotFoundException> {
                habitActor.updatesHabit(nonExistingHabitId, createHabitDTO())
            }
        }
    }

    private fun createHabitDTO(title: String = "Gym", description: String = "Go to the gym") = HabitDTO(title = title, description = description)
}
