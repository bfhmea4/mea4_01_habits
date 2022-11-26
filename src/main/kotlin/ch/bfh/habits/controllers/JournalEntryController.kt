package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.journalentry.JournalEntryDTO
import ch.bfh.habits.dtos.journalentry.JournalEntryListDTO
import ch.bfh.habits.services.JournalEntryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class JournalEntryController @Autowired constructor(private val journalEntryService: JournalEntryService) {
    @GetMapping("/api/habit/{id}/journal_entries")
    fun getAllJournalEntriesForHabit(@PathVariable id: Long): ResponseEntity<JournalEntryListDTO> {
        return ResponseEntity.ok().body(journalEntryService.getAllJournalEntriesForHabit(id))
    }

    @GetMapping("/api/journal_entries")
    fun getAllJournalEntries(): ResponseEntity<JournalEntryListDTO> {
        return ResponseEntity.ok().body(journalEntryService.getAllJournalEntries())
    }

    @PostMapping("/api/journal_entry")
    fun newJournalEntry(@RequestBody journalEntryDTO: JournalEntryDTO): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntryService.newJournalEntry(journalEntryDTO))
    }

    @GetMapping("/api/journal_entry/{id}")
    fun getJournalEntry(@PathVariable id: Long): ResponseEntity<JournalEntryDTO> {
        return ResponseEntity.ok().body(journalEntryService.getJournalEntryById(id))
    }

    @DeleteMapping("/api/journal_entry/{id}")
    fun deleteJournalEntry(@PathVariable id: Long): ResponseEntity<Any> {
        journalEntryService.deleteJournalEntryById(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/api/journal_entry/{id}")
    fun updateJournalEntry(@RequestBody journalEntry: JournalEntryDTO, @PathVariable id: Long): ResponseEntity<Any> {
        journalEntryService.updateJournalEntryById(id, journalEntry)
        return ResponseEntity.ok().build()
    }
}
