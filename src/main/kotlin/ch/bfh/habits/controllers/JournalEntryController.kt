package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.entities.JournalEntry
import ch.bfh.habits.services.JournalEntryService
import ch.bfh.habits.util.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class JournalEntryController @Autowired constructor(private val journalEntryService: JournalEntryService, private val tokenProvider: TokenProvider) {
    @GetMapping("/api/habit/{id}/journal_entries")
    fun getAllJournalEntriesForHabit(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<List<JournalEntry>> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(journalEntryService.getAllJournalEntriesForHabit(id, userId))
    }

    @GetMapping("/api/journal_entries")
    fun getAllJournalEntries(@RequestHeader(value = "Authorization") token: String): ResponseEntity<List<JournalEntry>> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(journalEntryService.getAllJournalEntries(userId))
    }

    @PostMapping("/api/journal_entry")
    fun newJournalEntry(@RequestHeader(value = "Authorization") token: String, @RequestBody journalEntryDTO: JournalEntryDTO): ResponseEntity<JournalEntry> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntryService.newJournalEntry(journalEntryDTO, userId))
    }

    @GetMapping("/api/journal_entry/{id}")
    fun getJournalEntry(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<JournalEntry> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(journalEntryService.getJournalEntry(id, userId))
    }

    @DeleteMapping("/api/journal_entry/{id}")
    fun deleteJournalEntry(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<Unit> {
        val userId = tokenProvider.extractId(token)
        journalEntryService.deleteJournalEntry(id, userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/api/journal_entry/{id}")
    fun updateJournalEntry(@RequestHeader(value = "Authorization") token: String, @RequestBody journalEntry: JournalEntryDTO, @PathVariable id: Long): ResponseEntity<JournalEntry> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(journalEntryService.updateJournalEntry(id, journalEntry, userId))
    }
}
