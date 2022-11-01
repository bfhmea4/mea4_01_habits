package ch.bfh.habits.journalentry

import ch.bfh.habits.controllers.JournalEntryController
import ch.bfh.habits.journalentry.impl.WebClientBasedJournalEntryCrudActor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class JournalEntryTestConfiguration {

    @Value("\${habits.test.localhost:false}")
    private var localhost: Boolean = false

    @Bean
    fun journalEntryActor(controller: JournalEntryController): JournalEntryCrudActor {
        val client =  if (localhost) {
            WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
        } else {
            WebTestClient.bindToController(controller).configureClient().build()
        }
        return WebClientBasedJournalEntryCrudActor(client)
    }

}
