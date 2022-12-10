package ch.bfh.habits.journalentry

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.habit.HabitCrudActor
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
class JournalEntryCrudFeatureTests {

    @Autowired
    private lateinit var habitActor: HabitCrudActor

    @Autowired
    private lateinit var journalEntryActor: JournalEntryCrudActor

    @Nested
    @DisplayName("Given no journal entries exist THEN we")
    inner class GivenNoJournalEntriesExists {
        @Test
        fun `get an empty list for get all`() {
            assertThat(journalEntryActor.getsAllJournalEntries()).isEqualTo(arrayListOf<JournalEntry>())
        }
    }

    @Nested
    @DisplayName("Given we have created a journal entry THEN we ...")
    inner class GivenNewJournalEntryCreated {
        private var habit = habitActor.createsHabit(createHabitDTO("Gym", "Go to the gym"))
        private var journalEntry = journalEntryActor.createsJournalEntry(createJournalEntryDTO("Done", habit.id!!))

        @Test
        fun `can find it amongst all journal entries`() {
            // when
            journalEntryActor.createsJournalEntry(createJournalEntryDTO("Done again", habit.id!!))
            val allJournalEntries = journalEntryActor.getsAllJournalEntries()

            // then
            assertThat(allJournalEntries.size).isGreaterThan(1)
            assertThat(allJournalEntries.filter { i -> i.id == journalEntry.id }).isNotEmpty
        }

        @Test
        fun `can find it`() {
            assertThat(journalEntryActor.seesJournalEntryExists(journalEntry.id!!)).isTrue
        }

        @Test
        fun `can get it`() {
            assertThat(journalEntryActor.getsJournalEntry(journalEntry.id!!).id).isEqualTo(journalEntry.id!!)
        }

        @Test
        fun `can delete it`() {
            journalEntryActor.deletesJournalEntry(journalEntry.id!!)
            assertThat(journalEntryActor.seesJournalEntryExists(journalEntry.id!!)).isFalse
        }

        @Test
        fun `can update it`() {
            // given
            val newJournalEntry = createJournalEntryDTO("Done again", habit.id!!)
            // when
            journalEntryActor.updatesJournalEntry(journalEntry.id!!, newJournalEntry)
            // then
            val updatedJournalEntry = journalEntryActor.getsJournalEntry(journalEntry.id!!)
            assertThat(updatedJournalEntry.description).isEqualTo("Done again")
        }
    }

    @Nested
    @DisplayName("Given a non-existing journal entry id THEN ...")
    inner class GivenNonExistingJournalEntry{
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
