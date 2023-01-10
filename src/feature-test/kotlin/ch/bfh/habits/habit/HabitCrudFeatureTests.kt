package ch.bfh.habits.habit

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HabitCrudFeatureTests {
    @Autowired
    private lateinit var habitActor: HabitCrudActor

    @Nested
    @DisplayName("Given no habits exist THEN we")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    inner class GivenNoHabitExists {
        @BeforeAll
        fun setup() {
            habitActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            habitActor.login(LoginDTO("test", "test"))
        }

        @Test
        fun `get an empty list for get all`() {
            assertThat(habitActor.getsAllHabits()).isEqualTo(arrayListOf<Habit>())
        }
    }

    @Nested
    @DisplayName("Given we have created a habit THEN we ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @TestMethodOrder(OrderAnnotation::class)
    inner class GivenNewHabitCreated {
        private lateinit var habit: Habit

        @BeforeAll
        fun setup() {
            habitActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            habitActor.login(LoginDTO("test", "test"))
            habit = habitActor.createsHabit(createHabitDTO())
        }

        @Test
        @Order(1)
        fun `can find it amongst all habits`() {
            // when
            habitActor.createsHabit(createHabitDTO("Running", "Go for a run"))
            val allHabits = habitActor.getsAllHabits()

            // then
            assertThat(allHabits.size).isGreaterThan(1)
            assertThat(allHabits.filter { i -> i.id == habit.id }).isNotEmpty
        }

        @Test
        @Order(2)
        fun `can find it`() {
            assertThat(habitActor.seesHabitExists(habit.id!!)).isTrue
        }

        @Test
        @Order(3)
        fun `can get it`() {
            assertThat(habitActor.getsHabit(habit.id!!).id).isEqualTo(habit.id)
        }

        @Test
        @Order(4)
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

        @Test
        @Order(5)
        fun `can delete it`() {
            habitActor.deletesHabit(habit.id!!)
            assertThat(habitActor.seesHabitExists(habit.id!!)).isFalse
        }
    }

    @Nested
    @DisplayName("Given a non-existing habit id THEN ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    inner class GivenNonExistingHabit {
        @BeforeAll
        fun setup() {
            habitActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            habitActor.login(LoginDTO("test", "test"))
        }

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

    private fun createHabitDTO(title: String = "Gym", description: String = "Go to the gym") = HabitDTO(title = title, description = description)
}
