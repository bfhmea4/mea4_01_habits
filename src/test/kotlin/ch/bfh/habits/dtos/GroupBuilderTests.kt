package ch.bfh.habits.dtos

import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.dtos.group.GroupEntityBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GroupBuilderTests {
    @Test
    fun empty_dto_creates_default_group() {
        val entity = GroupEntityBuilder.createGroupEntityFromDTO(GroupDTO(), 1)
        Assertions.assertThat(entity.title).isEqualTo("")
        Assertions.assertThat(entity.userId).isEqualTo(1)
    }
}
