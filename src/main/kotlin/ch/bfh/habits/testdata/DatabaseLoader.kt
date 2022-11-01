package ch.bfh.habits.testdata

import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.repositories.HabitDAO
import ch.bfh.habits.repositories.JournalEntryDAO
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseLoader(
    private val habitDAO: HabitDAO,
    private val journalEntryDAO: JournalEntryDAO
) : CommandLineRunner  {
    override fun run(vararg args: String?) {
        // val readingHabit = Habit("Reading", "Read more books")
        // val runningHabit = Habit("Run", "Prepare for Berlin Marathon")
        // val readingJournalEntry = JournalEntry("Read 50 pages", readingHabit)
        // val runningJournalEntry = JournalEntry("Ran a half marathon for training", runningHabit)

        // habitDAO.save(readingHabit)
        // habitDAO.save(runningHabit)
        // journalEntryDAO.save(readingJournalEntry)
        // journalEntryDAO.save(runningJournalEntry)
    }
}
