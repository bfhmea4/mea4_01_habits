package ch.bfh.habits.habit.impl

import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.habit.HabitCrudActor
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

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
}
