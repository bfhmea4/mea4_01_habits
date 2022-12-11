package ch.bfh.habits.journalentry

import ch.bfh.habits.controllers.AuthController
import ch.bfh.habits.controllers.JournalEntryController
import ch.bfh.habits.journalentry.impl.WebClientBasedJournalEntryCrudActor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class JournalEntryTestConfiguration {
    @Bean
    fun journalEntryActor(controller: JournalEntryController, authController: AuthController): JournalEntryCrudActor {
        val client = WebTestClient.bindToController(controller).configureClient().build()
        val authClient = WebTestClient.bindToController(authController).configureClient().build()
        return WebClientBasedJournalEntryCrudActor(client, authClient)
    }

}
