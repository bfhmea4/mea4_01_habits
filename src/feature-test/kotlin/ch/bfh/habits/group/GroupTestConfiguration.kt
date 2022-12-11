package ch.bfh.habits.group

import ch.bfh.habits.controllers.AuthController
import ch.bfh.habits.controllers.GroupController
import ch.bfh.habits.group.impl.WebClientBasedGroupCrudActor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class GroupTestConfiguration {
    @Bean
    fun groupActor(controller: GroupController, authController: AuthController): GroupCrudActor {
        val client = WebTestClient.bindToController(controller).configureClient().build()
        val authClient = WebTestClient.bindToController(authController).configureClient().build()
        return WebClientBasedGroupCrudActor(client, authClient)
    }
}
