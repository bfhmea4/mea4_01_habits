package ch.bfh.habits.journalentry

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.habit.HabitCrudActor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JournalEntryCrudFeatureTests {
    @Autowired
    private lateinit var habitActor: HabitCrudActor

    @Autowired
    private lateinit var journalEntryActor: JournalEntryCrudActor

    @Nested
    @DisplayName("Given no journal entries exist THEN we")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    inner class GivenNoJournalEntriesExists {
        @BeforeAll
        fun setup() {
            journalEntryActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            journalEntryActor.login(LoginDTO("test", "test"))
        }

        @Test
        fun `get an empty list for get all`() {
            assertThat(journalEntryActor.getsAllJournalEntries()).isEqualTo(arrayListOf<JournalEntry>())
        }
    }

    @Nested
    @DisplayName("Given we have created a journal entry THEN we ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @TestMethodOrder(OrderAnnotation::class)
    inner class GivenNewJournalEntryCreated {
        private lateinit var habit: Habit
        private lateinit var journalEntry: JournalEntry
        @BeforeAll
        fun setup() {
            habitActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            habitActor.login(LoginDTO("test", "test"))
            journalEntryActor.login(LoginDTO("test", "test"))

            habit = habitActor.createsHabit(createHabitDTO("Gym", "Go to the gym"))
            journalEntry = journalEntryActor.createsJournalEntry(createJournalEntryDTO("Done", habit.id!!))
        }

        @Test
        @Order(1)
        fun `can find it amongst all journal entries`() {
            // when
            journalEntryActor.createsJournalEntry(createJournalEntryDTO("Done again", habit.id!!))
            val allJournalEntries = journalEntryActor.getsAllJournalEntries()

            // then
            assertThat(allJournalEntries.size).isGreaterThan(1)
            assertThat(allJournalEntries.filter { i -> i.id == journalEntry.id }).isNotEmpty
        }

        @Test
        @Order(2)
        fun `can find it`() {
            assertThat(journalEntryActor.seesJournalEntryExists(journalEntry.id!!)).isTrue
        }

        @Test
        @Order(3)
        fun `can get it`() {
            assertThat(journalEntryActor.getsJournalEntry(journalEntry.id!!).id).isEqualTo(journalEntry.id!!)
        }

        @Test
        @Order(4)
        fun `can update it`() {
            // given
            val newJournalEntry = createJournalEntryDTO("Done again", habit.id!!)
            // when
            journalEntryActor.updatesJournalEntry(journalEntry.id!!, newJournalEntry)
            // then
            val updatedJournalEntry = journalEntryActor.getsJournalEntry(journalEntry.id!!)
            assertThat(updatedJournalEntry.description).isEqualTo("Done again")
        }

        @Test
        @Order(5)
        fun `can delete it`() {
            journalEntryActor.deletesJournalEntry(journalEntry.id!!)
            assertThat(journalEntryActor.seesJournalEntryExists(journalEntry.id!!)).isFalse
        }
    }

    @Nested
    @DisplayName("Given a non-existing journal entry id THEN ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    inner class GivenNonExistingJournalEntry {
        @BeforeAll
        fun setup() {
            journalEntryActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            journalEntryActor.login(LoginDTO("test", "test"))
        }

        private val nonExistingJournalEntryId = -1L

        @Test
        fun `find returns false`() {
            assertThat(journalEntryActor.seesJournalEntryExists(nonExistingJournalEntryId)).isFalse
        }

        @Test
        fun `get throws not found`() {
            assertThrows<EntityNotFoundException> {
                journalEntryActor.getsJournalEntry(nonExistingJournalEntryId)
            }
        }

        @Test
        fun `delete throws not found`() {
            assertThrows<EntityNotFoundException> {
                journalEntryActor.deletesJournalEntry(nonExistingJournalEntryId)
            }
        }

        @Test
        fun `update throws not found`() {
            assertThrows<EntityNotFoundException> {
                journalEntryActor.updatesJournalEntry(nonExistingJournalEntryId, createJournalEntryDTO())
            }
        }
    }

    private fun createHabitDTO(title: String = "", description: String = "") = HabitDTO(title = title, description = description)

    private fun createJournalEntryDTO(description: String = "", habitId: Long? = null) = JournalEntryDTO(description = description, habitId = habitId)
}
