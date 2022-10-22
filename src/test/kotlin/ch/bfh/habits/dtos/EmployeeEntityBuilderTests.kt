package ch.bfh.habits.dtos

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class EmployeeEntityBuilderTests {

    @Test
    fun empty_dto_creates_default_employee() {
        val entity = EmployeeEntityBuilder.createEmployeeEntityFromDTO(EmployeeDTO())
        assertThat(entity.name).isEqualTo("")
    }

}
