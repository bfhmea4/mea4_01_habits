package ch.bfh.habits.habit

import ch.bfh.habits.controllers.HabitController
import ch.bfh.habits.habit.impl.WebClientBasedHabitCrudActor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class HabitTestConfiguration {

    @Value("\${habits.test.localhost:false}")
    private var localhost: Boolean = false

    @Bean
    fun habitActor(controller: HabitController): HabitCrudActor {
        val client =  if (localhost) {
            WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
        } else {
            WebTestClient.bindToController(controller).configureClient().build()
        }
        return WebClientBasedHabitCrudActor(client)
    }

}
