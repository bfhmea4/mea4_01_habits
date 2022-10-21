package ch.bfh.habits.employees

import ch.bfh.habits.dtos.EmployeeDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class EmployeeCrudFeatureTests {

    @Autowired
    private lateinit var actor: EmployeeCrudActor

    @Nested
    @DisplayName("Given we have created an employee THEN we ...")
    inner class GivenNewEmployee {
        private val employeeId = actor.createsEmployee(createEmployeeDTO(name="John"))

        @Test
        fun `can find it`() {
            assertThat(actor.seesEmployeeExists(employeeId)).isTrue
        }

        @Test
        fun `can get it`() {
            assertThat(actor.getsEmployee(employeeId).id).isEqualTo(employeeId)
        }

        @Test
        fun `can delete it`() {
            actor.deletesEmployee(employeeId)
            assertThat(actor.seesEmployeeExists(employeeId)).isFalse
        }

        @Test
        fun `can update it`() {
            // given
            val originalEmployee = actor.getsEmployee(employeeId)
            // when
            actor.updatesEmployee(employeeId, originalEmployee.copy(name = "Something new"))
            // then
            val updatedEmployee = actor.getsEmployee(employeeId)
            assertThat(updatedEmployee.name).isEqualTo("Something new")
        }

        @Test
        fun `can find it amongst all employees`() {
            actor.createsEmployee(createEmployeeDTO())
            val allEmployees = actor.getsAllEmployees().employees

            // then
            assertThat(allEmployees.size).isGreaterThan(1)
            assertThat(allEmployees.filter { i -> i.id == employeeId }).isNotEmpty
        }
    }

    @Nested
    @DisplayName("Given a non-existing employee id THEN ...")
    inner class GivenNonExistingEmployee {
        private val nonExistingEmployeeId = -1L

        @Test
        fun `find returns false`() {
            assertThat(actor.seesEmployeeExists(nonExistingEmployeeId)).isFalse
        }

        @Test
        fun `get throws not found`() {
            assertThrows<EntityNotFoundException> {
                actor.getsEmployee(nonExistingEmployeeId)
            }
        }

        @Test
        fun `update throws not found`() {
            assertThrows<EntityNotFoundException> {
                actor.updatesEmployee(nonExistingEmployeeId, createEmployeeDTO())
            }
        }

        @Test
        fun `delete throws not found`() {
            assertThrows<EntityNotFoundException> {
                actor.deletesEmployee(nonExistingEmployeeId)
            }
        }

    }

    private fun createEmployeeDTO(name: String = "") = EmployeeDTO(name = name)
}
