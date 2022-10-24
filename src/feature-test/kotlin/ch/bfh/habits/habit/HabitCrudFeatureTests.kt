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
            assertThat(habitActor.getAllHabits()).isEqualTo(HabitListDTO(arrayListOf()))
        }
    }

    @Nested
    @DisplayName("Given we have created a habit THEN we ...")
    inner class GivenNewHabitCreated {
        private var habitId = habitActor.newHabit(createHabitDTO("Gym", "Go to the Gym more often"))

        @Test
        fun `can find it amongst all habits`() {
            // when
            habitActor.newHabit(createHabitDTO("Running", "Go run"))
            val allHabits = habitActor.getAllHabits().habits

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
    }

    private fun createHabitDTO(title: String = "", description: String = "") = HabitDTO(title = title, description = description, completed = false)
}
