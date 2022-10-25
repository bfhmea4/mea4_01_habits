package ch.bfh.habits.journalentry.impl

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.journalentry.JournalEntryCrudActor
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Mono

class WebClientBasedJournalEntryCrudActor(private val webClient: WebTestClient) : JournalEntryCrudActor {
    override fun getsAllJournalEntriesForHabit(habitId: Long): JournalEntryListDTO {
        val result = webClient.get()
            .uri("/api/habit/$habitId/journal_entries")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<JournalEntryListDTO>()
            .returnResult()

        return result.responseBody!!
    }

    override fun getsAllJournalEntries(): JournalEntryListDTO {
        val result = webClient.get()
            .uri("/api/journal_entries")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<JournalEntryListDTO>()
            .returnResult()

        return result.responseBody!!
    }

    override fun createsJournalEntry(journalEntryDTO: JournalEntryDTO): Long {
        val result = webClient.post()
            .uri("/api/journal_entry")
            .body(Mono.just(journalEntryDTO), JournalEntryDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<ObjectIdDTO>()
            .returnResult().responseBody

        return result!!.id
    }

    override fun seesJournalEntryExists(journalEntryId: Long): Boolean {
        return webClient.get()
            .uri("/api/journal_entry/$journalEntryId")
            .exchange()
            .returnResult<Any>()
            .status
            .is2xxSuccessful
    }

    override fun getsJournalEntry(journalEntryId: Long): JournalEntryDTO {
        val result = webClient.get()
            .uri("/api/journal_entry/$journalEntryId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody<JournalEntryDTO>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Journal Entry id = $journalEntryId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }

    override fun deletesJournalEntry(journalEntryId: Long) {
        val result = webClient.delete()
            .uri("/api/journal_entry/$journalEntryId")
            .exchange()
            .returnResult<Any>()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Journal Entry id = $journalEntryId")

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.NO_CONTENT)
    }

    override fun updatesJournalEntry(journalEntryId: Long, journalEntryDTO: JournalEntryDTO) {
        val result = webClient.put()
            .uri("/api/journal_entry/$journalEntryId")
            .body(Mono.just(journalEntryDTO), JournalEntryDTO::class.java)
            .exchange()
            .expectBody()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Journal Entry id = $journalEntryId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
    }
}
