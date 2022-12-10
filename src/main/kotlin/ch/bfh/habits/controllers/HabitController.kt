package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.services.HabitService
import ch.bfh.habits.util.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class HabitController @Autowired constructor(private val service: HabitService, private val tokenProvider: TokenProvider) {
    @GetMapping("/api/habits")
    fun getAllHabits(@RequestHeader(value = "Authorization") token: String): ResponseEntity<HabitListDTO> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(service.getAllHabits(userId))
    }

    @PostMapping("/api/habit", consumes = ["application/json"])
    fun newHabit(@RequestHeader(value = "Authorization") token: String, @RequestBody habitDTO: HabitDTO): ResponseEntity<Any> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.status(HttpStatus.CREATED).body(service.newHabit(habitDTO, userId))
    }

    @GetMapping("/api/habit/{id}")
    fun getHabit(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<HabitDTO> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(service.getHabit(id, userId))
    }

    @DeleteMapping("/api/habit/{id}")
    fun deleteHabit(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<Any> {
        val userId = tokenProvider.extractId(token)
        service.deleteHabit(id, userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/api/habit/{id}")
    fun updateHabit(@RequestHeader(value = "Authorization") token: String, @RequestBody habit: HabitDTO, @PathVariable id: Long): ResponseEntity<Any> {
        val userId = tokenProvider.extractId(token)
        service.updateHabit(id, habit, userId)
        return ResponseEntity.ok().build()
    }
}
