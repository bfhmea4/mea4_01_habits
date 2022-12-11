package ch.bfh.habits.base.impl

import ch.bfh.habits.base.BaseCrudActor
import ch.bfh.habits.dtos.user.JwtTokenDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.User
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

open class WebClientBasedBaseCrudActor(private val webClient: WebTestClient) : BaseCrudActor {
    internal var token = ""

    override fun login(input: LoginDTO): JwtTokenDTO {
        val result = webClient.post()
            .uri("/api/login")
            .body(Mono.just(input), LoginDTO::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody<JwtTokenDTO>()
            .returnResult()

        token = result.responseBody!!.token

        return result.responseBody!!
    }

    override fun register(input: RegisterDTO): User {
        val result = webClient.post()
            .uri("/api/register")
            .body(Mono.just(input), RegisterDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<User>()
            .returnResult()

        return result.responseBody!!
    }
}
