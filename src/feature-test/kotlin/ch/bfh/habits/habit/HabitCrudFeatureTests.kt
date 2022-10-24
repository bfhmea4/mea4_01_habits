package ch.bfh.habits.habit

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class HabitCrudFeatureTests {

    @Autowired
    private lateinit var habitActor: HabitCrudActor

    @Nested
    @DisplayName("Given no habits exist THEN we")
    inner class GivenNoHabitExists {
        @Test
        fun `get an empty list for get all`() {
            assertThat(habitActor.getsAllHabits()).isEqualTo(HabitListDTO(arrayListOf()))
        }
    }

    @Nested
    @DisplayName("Given we have created a habit THEN we ...")
    inner class GivenNewHabitCreated {
        private var habitId = habitActor.createsHabit(createHabitDTO("Gym", "Go to the Gym more often"))

        @Test
        fun `can find it amongst all habits`() {
            // when
            habitActor.createsHabit(createHabitDTO("Running", "Go run"))
            val allHabits = habitActor.getsAllHabits().habits

            // then
            assertThat(allHabits.size).isGreaterThan(1)
            assertThat(allHabits.filter { i -> i.id == habitId }).isNotEmpty
        }

        @Test
        fun `can find it`() {
            assertThat(habitActor.seesHabitExists(habitId)).isTrue
        }

        @Test
        fun `can get it`() {
            assertThat(habitActor.getsHabit(habitId).id).isEqualTo(habitId)
        }

        @Test
        fun `can delete it`() {
            habitActor.deletesHabit(habitId)
            assertThat(habitActor.seesHabitExists(habitId)).isFalse
        }

        @Test
        fun `can update it`() {
            // given
            val originalHabit = habitActor.getsHabit(habitId)
            // when
            habitActor.updatesHabit(habitId, originalHabit.copy(title = "Gym_Old"))
            // then
            val updatedHabit = habitActor.getsHabit(habitId)
            assertThat(updatedHabit.title).isEqualTo("Gym_Old")
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
        fun `update throws not found`() {
            assertThrows<EntityNotFoundException> {
                habitActor.updatesHabit(nonExistingHabitId, createHabitDTO())
            }
        }
    }

    private fun createHabitDTO(title: String = "", description: String = "") = HabitDTO(title = title, description = description, completed = false)
}
