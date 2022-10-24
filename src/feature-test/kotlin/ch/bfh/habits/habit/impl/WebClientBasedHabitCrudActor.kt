package ch.bfh.habits.habit.impl

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.habit.HabitCrudActor
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Mono

class WebClientBasedHabitCrudActor(private val webClient: WebTestClient) : HabitCrudActor {
    override fun getAllHabits(): HabitListDTO {
        val result = webClient.get()
            .uri("/api/habits/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<HabitListDTO>()
            .returnResult()

        return result.responseBody!!
    }

    override fun newHabit(habitDTO: HabitDTO): Long {
        val result = webClient.post()
            .uri("/api/habit")
            .body(Mono.just(habitDTO), HabitDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<ObjectIdDTO>()
            .returnResult().responseBody

        return result!!.id
    }

    override fun seesHabitExists(habitId: Long): Boolean {
        return webClient.get()
            .uri("/api/habit/$habitId")
            .exchange()
            .returnResult<Any>()
            .status
            .is2xxSuccessful
    }

    override fun getsHabit(habitId: Long): HabitDTO {
        val result = webClient.get()
            .uri("/api/habit/$habitId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody<HabitDTO>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Habit id = $habitId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!

    }
}
