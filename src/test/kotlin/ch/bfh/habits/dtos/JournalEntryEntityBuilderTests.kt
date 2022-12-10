package ch.bfh.habits.dtos

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryEntityBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class JournalEntryEntityBuilderTests {

    @Test
    fun empty_dto_creates_default_habit() {
        val entity = JournalEntryEntityBuilder.createJournalEntryEntityFromDTO(JournalEntryDTO(), 1)
        assertThat(entity.description).isEqualTo("")
        assertThat(entity.habit).isNull()
        assertThat(entity.userId).isEqualTo(1)
    }
}
