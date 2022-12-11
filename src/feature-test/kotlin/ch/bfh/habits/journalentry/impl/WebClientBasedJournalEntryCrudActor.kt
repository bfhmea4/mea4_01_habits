package ch.bfh.habits.journalentry.impl

import ch.bfh.habits.auth.impl.WebClientBasedAuthCrudActor
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.journalentry.JournalEntryCrudActor
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Mono

class WebClientBasedJournalEntryCrudActor(private val webClient: WebTestClient, authWebClient: WebTestClient) : WebClientBasedAuthCrudActor(authWebClient), JournalEntryCrudActor {
    override fun getsAllJournalEntriesForHabit(habitId: Long): List<JournalEntry> {
        val result = webClient.get()
            .uri("/api/habit/$habitId/journal_entries")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<List<JournalEntry>>()
            .returnResult()

        return result.responseBody!!
    }

    override fun getsAllJournalEntries(): List<JournalEntry> {
        val result = webClient.get()
            .uri("/api/journal_entries")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<List<JournalEntry>>()
            .returnResult()

        return result.responseBody!!
    }

    override fun createsJournalEntry(journalEntryDTO: JournalEntryDTO): JournalEntry {
        val result = webClient.post()
            .uri("/api/journal_entry")
            .header("Authorization", token)
            .body(Mono.just(journalEntryDTO), JournalEntryDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<JournalEntry>()
            .returnResult()

        return result.responseBody!!
    }

    override fun seesJournalEntryExists(journalEntryId: Long): Boolean {
        return webClient.get()
            .uri("/api/journal_entry/$journalEntryId")
            .header("Authorization", token)
            .exchange()
            .returnResult<Any>()
            .status
            .is2xxSuccessful
    }

    override fun getsJournalEntry(journalEntryId: Long): JournalEntry {
        val result = webClient.get()
            .uri("/api/journal_entry/$journalEntryId")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody<JournalEntry>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Journal Entry id = $journalEntryId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }

    override fun deletesJournalEntry(journalEntryId: Long) {
        val result = webClient.delete()
            .uri("/api/journal_entry/$journalEntryId")
            .header("Authorization", token)
            .exchange()
            .returnResult<Any>()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Journal Entry id = $journalEntryId")

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.NO_CONTENT)
    }

    override fun updatesJournalEntry(journalEntryId: Long, journalEntryDTO: JournalEntryDTO): JournalEntry {
        val result = webClient.put()
            .uri("/api/journal_entry/$journalEntryId")
            .header("Authorization", token)
            .body(Mono.just(journalEntryDTO), JournalEntryDTO::class.java)
            .exchange()
            .expectBody<JournalEntry>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Journal Entry id = $journalEntryId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }
}
