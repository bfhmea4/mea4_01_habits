package ch.bfh.habits.employees.impl

import ch.bfh.habits.dtos.EmployeeDTO
import ch.bfh.habits.dtos.EmployeeListDTO
import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.employees.EmployeeCrudActor
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Mono

class WebClientBasedEmployeeCrudActor(private val webClient: WebTestClient) : EmployeeCrudActor {

    override fun createsEmployee(employee: EmployeeDTO): Long {
        val result = webClient.post()
            .uri("/api/employees")
            .body(Mono.just(employee), EmployeeDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<ObjectIdDTO>()
            .returnResult().responseBody

        return result!!.id
    }

    override fun seesEmployeeExists(employeeId: Long): Boolean {
        return webClient.get()
            .uri("/api/employees/$employeeId")
            .exchange()
            .returnResult<Any>()
            .status
            .is2xxSuccessful
    }

    override fun getsEmployee(employeeId: Long): EmployeeDTO {
        val result = webClient.get()
            .uri("/api/employees/$employeeId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody<EmployeeDTO>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Employee id = $employeeId")

        assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!

    }

    override fun updatesEmployee(employeeId: Long, employee: EmployeeDTO) {
        val result = webClient.put()
            .uri("/api/employees/$employeeId")
            .body(Mono.just(employee), EmployeeDTO::class.java)
            .exchange()
            .expectBody()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Employee id = $employeeId")

        assertThat(result.status.is2xxSuccessful).isTrue
    }

    override fun deletesEmployee(employeeId: Long) {
        val result = webClient.delete()
            .uri("/api/employees/$employeeId")
            .exchange()
            .returnResult<Any>()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Employee id = $employeeId")

        assertThat(result.status).isEqualTo(HttpStatus.NO_CONTENT)
    }

    override fun getsAllEmployees(): EmployeeListDTO {
        val result = webClient.get()
            .uri("/api/employees/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<EmployeeListDTO>()
            .returnResult()

        return result.responseBody!!
    }

}
