package ch.bfh.habits.testdata

import ch.bfh.habits.entities.Habit
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.entities.User
import ch.bfh.habits.repositories.HabitDAO
import ch.bfh.habits.repositories.JournalEntryDAO
import ch.bfh.habits.repositories.UserDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component


@Component
class DatabaseLoader(
    private val habitDAO: HabitDAO,
    private val journalEntryDAO: JournalEntryDAO,
    private val userDAO: UserDAO,
) : CommandLineRunner  {

    @Autowired
    private val environment: Environment? = null

    override fun run(vararg args: String?) {
        if (habitDAO.count() == 0L && environment?.activeProfiles?.contains("local") == true) {
            val user = User("John", "Doe", "johnD", "john@doe.com")
            user.password = "root"
            userDAO.save(user)

            val habit1 = Habit("Meditation", "Meditate for 10 minutes", userId = user.id ?: 1)
            val habit2 = Habit("Exercise", "Go for a run", userId = user.id ?: 1)
            val habit3 = Habit("Reading", "Read a book", userId = user.id ?: 1)
            habitDAO.saveAll(listOf(habit1, habit2, habit3))

            val journalEntry1 = JournalEntry("Read 50 pages", habit3, user.id ?: 1)
            val journalEntry2 = JournalEntry("Ran a half marathon for training", habit2, user.id ?: 1)
            val journalEntry3 = JournalEntry("Just something", null, user.id ?: 1)
            journalEntryDAO.saveAll(listOf(journalEntry1, journalEntry2, journalEntry3))
        }
    }
}
