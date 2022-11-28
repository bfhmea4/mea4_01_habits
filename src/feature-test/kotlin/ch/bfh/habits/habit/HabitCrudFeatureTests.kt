package ch.bfh.habits.habit

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
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
            assertThat(habitActor.getsAllHabits()).isEqualTo(HabitListDTO(arrayListOf()))
        }
    }

    @Nested
    @DisplayName("Given we have created a habit THEN we ...")
    inner class GivenNewHabitCreated {
        private var habitId = habitActor.createsHabit(createHabitDTO())

        @Test
        fun `can find it amongst all habits`() {
            // when
            habitActor.createsHabit(createHabitDTO("Running", "Go for a run"))
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
        fun `update throws bad request`() {
            assertThrows<BadRequestException> {
                habitActor.updatesHabit(nonExistingHabitId, createHabitDTO())
            }
        }

        @Test
        fun `update throws not found`() {
            assertThrows<EntityNotFoundException> {
                habitActor.updatesHabit(nonExistingHabitId, createHabitDTO2())
            }
        }
    }

    private fun createHabitDTO(title: String = "Gym", description: String = "Go to the gym") = HabitDTO(title = title, description = description)
    private fun createHabitDTO2(id: Long = -1L, title: String = "Gym", description: String = "Go to the gym") = HabitDTO(id = id, title = title, description = description)
}
