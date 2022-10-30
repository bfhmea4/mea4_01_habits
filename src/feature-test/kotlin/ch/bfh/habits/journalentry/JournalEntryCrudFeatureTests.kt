package ch.bfh.habits.journalentry

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
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
            assertThat(journalEntryActor.getsAllJournalEntries()).isEqualTo(JournalEntryListDTO(arrayListOf()))
        }
    }

    @Nested
    @DisplayName("Given we have created a journal entry THEN we ...")
    inner class GivenNewJournalEntryCreated {
        private var habitId = habitActor.createsHabit(createHabitDTO("Gym"))
        private var journalEntryId = journalEntryActor.createsJournalEntry(createJournalEntryDTO("Done", habitId))

        @Test
        fun `can find it amongst all journal entries`() {
            // when
            journalEntryActor.createsJournalEntry(createJournalEntryDTO("Done again", habitId))
            val allJournalEntries = journalEntryActor.getsAllJournalEntries().journalEntries

            // then
            assertThat(allJournalEntries.size).isGreaterThan(1)
            assertThat(allJournalEntries.filter { i -> i.id == journalEntryId }).isNotEmpty
        }

        @Test
        fun `can find it`() {
            assertThat(journalEntryActor.seesJournalEntryExists(journalEntryId)).isTrue
        }

        @Test
        fun `can get it`() {
            assertThat(journalEntryActor.getsJournalEntry(journalEntryId).id).isEqualTo(journalEntryId)
        }

        @Test
        fun `can delete it`() {
            journalEntryActor.deletesJournalEntry(journalEntryId)
            assertThat(journalEntryActor.seesJournalEntryExists(journalEntryId)).isFalse
        }

        @Test
        fun `can update it`() {
            // given
            val originalJournalEntry = journalEntryActor.getsJournalEntry(journalEntryId)
            // when
            // ToDo also check adding/removing/changing habit reference
            journalEntryActor.updatesJournalEntry(journalEntryId, originalJournalEntry.copy(description = "Done_Fixed"))
            // then
            val updatedJournalEntry = journalEntryActor.getsJournalEntry(journalEntryId)
            assertThat(updatedJournalEntry.description).isEqualTo("Done_Fixed")
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

    private fun createHabitDTO(title: String = "") = HabitDTO(title = title)

    private fun createJournalEntryDTO(description: String = "", habitId: Long? = null) = JournalEntryDTO(description = description, habitId = habitId)
}
