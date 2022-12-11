package ch.bfh.habits.auth.impl

import ch.bfh.habits.auth.AuthCrudActor
import ch.bfh.habits.dtos.user.JwtTokenDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.User
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

open class WebClientBasedAuthCrudActor(private val webClient: WebTestClient) : AuthCrudActor {
    internal var token = ""
        set(value) {
            field = "Bearer $value"
        }

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
