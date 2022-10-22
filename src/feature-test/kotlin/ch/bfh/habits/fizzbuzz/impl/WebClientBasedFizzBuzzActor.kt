package ch.bfh.habits.fizzbuzz.impl

import ch.bfh.habits.controllers.FizzBuzzController
import ch.bfh.habits.dtos.FizzBuzzResultDTO
import ch.bfh.habits.fizzbuzz.FizzBuzzActor
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

class WebClientBasedFizzBuzzActor(private val client: WebTestClient) : FizzBuzzActor {

    companion object {
        fun localHostServer(): WebClientBasedFizzBuzzActor {
            return WebClientBasedFizzBuzzActor(WebTestClient.bindToServer().baseUrl("http://localhost:8080").build())
        }
        fun mockServer(): WebClientBasedFizzBuzzActor {
            return WebClientBasedFizzBuzzActor(WebTestClient.bindToController(FizzBuzzController()).build())
        }
    }

    override fun getsFizzBuzzResult(n: Int): String {
        val responseBody: FizzBuzzResultDTO? = client.get()
            .uri("/api/fizzbuzz/$n")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectHeader().valueEquals("Content-Type", "application/json")
            .expectBody<FizzBuzzResultDTO>()
            .returnResult().responseBody

        return responseBody?.result ?: ""
    }
}
