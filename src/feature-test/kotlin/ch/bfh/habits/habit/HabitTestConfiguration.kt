package ch.bfh.habits.habit

import ch.bfh.habits.controllers.AuthController
import ch.bfh.habits.controllers.HabitController
import ch.bfh.habits.habit.impl.WebClientBasedHabitCrudActor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class HabitTestConfiguration {
    @Bean
    fun habitActor(controller: HabitController, authController: AuthController): HabitCrudActor {
        val client = WebTestClient.bindToController(controller).configureClient().build()
        val authClient = WebTestClient.bindToController(authController).configureClient().build()
        return WebClientBasedHabitCrudActor(client, authClient)
    }
}
