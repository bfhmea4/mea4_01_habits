package ch.bfh.habits.dtos

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitEntityBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class HabitEntityBuilderTests {

    @Test
    fun empty_dto_creates_default_habit() {
        val entity = HabitEntityBuilder.createHabitEntityFromDTO(HabitDTO(), 1)
        assertThat(entity.title).isEqualTo("")
        assertThat(entity.description).isEqualTo("")
        assertThat(entity.frequency).isNull()
        assertThat(entity.frequencyValue).isNull()
        assertThat(entity.userId).isEqualTo(1)
    }
}
