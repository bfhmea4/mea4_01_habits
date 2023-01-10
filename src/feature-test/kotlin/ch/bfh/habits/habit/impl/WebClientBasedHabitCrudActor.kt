package ch.bfh.habits.habit.impl

import ch.bfh.habits.auth.impl.WebClientBasedAuthCrudActor
import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.habit.HabitCrudActor
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Mono

class WebClientBasedHabitCrudActor(private val webClient: WebTestClient, authWebClient: WebTestClient) : WebClientBasedAuthCrudActor(authWebClient), HabitCrudActor {
    override fun getsAllHabits(): List<Habit> {
        val result = webClient.get()
            .uri("/api/habits/")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<List<Habit>>()
            .returnResult()

        return result.responseBody!!
    }

    override fun createsHabit(habitDTO: HabitDTO): Habit {
        val result = webClient.post()
            .uri("/api/habit")
            .header("Authorization", token)
            .body(Mono.just(habitDTO), HabitDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Habit>()
            .returnResult()

        return result.responseBody!!
    }

    override fun seesHabitExists(habitId: Long): Boolean {
        return webClient.get()
            .uri("/api/habit/$habitId")
            .header("Authorization", token)
            .exchange()
            .returnResult<Any>()
            .status
            .is2xxSuccessful
    }

    override fun getsHabit(habitId: Long): Habit {
        val result = webClient.get()
            .uri("/api/habit/$habitId")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody<Habit>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Habit id = $habitId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }

    override fun deletesHabit(habitId: Long) {
        val result = webClient.delete()
            .uri("/api/habit/$habitId")
            .header("Authorization", token)
            .exchange()
            .returnResult<Any>()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Habit id = $habitId")

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.NO_CONTENT)
    }

    override fun updatesHabit(habitId: Long, habitDTO: HabitDTO): Habit {
        val result = webClient.put()
            .uri("/api/habit/$habitId")
            .header("Authorization", token)
            .body(Mono.just(habitDTO), HabitDTO::class.java)
            .exchange()
            .expectBody<Habit>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Habit id = $habitId")
        else if (result.status == HttpStatus.BAD_REQUEST)
            throw BadRequestException("Habit id = $habitId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }
}
