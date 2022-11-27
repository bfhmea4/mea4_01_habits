package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.services.HabitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class HabitController @Autowired constructor(private val service: HabitService) {
    @GetMapping("/api/habits")
    fun getAllHabits(): ResponseEntity<HabitListDTO> {
        return ResponseEntity.ok().body(service.getAllHabits())
    }

    @PostMapping("/api/habit", consumes = ["application/json"])
    fun newHabit(@RequestBody habitDTO: HabitDTO): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.newHabit(habitDTO))
    }

    @GetMapping("/api/habit/{id}")
    fun getHabit(@PathVariable id: Long): ResponseEntity<HabitDTO> {
        return ResponseEntity.ok().body(service.getHabitById(id))
    }

    @DeleteMapping("/api/habit/{id}")
    fun deleteHabit(@PathVariable id: Long): ResponseEntity<Any> {
        service.deleteHabitById(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/api/habit/{id}")
    fun updateHabit(@RequestBody habit: HabitDTO, @PathVariable id: Long): ResponseEntity<Any> {
        service.updateHabitById(id, habit)
        return ResponseEntity.ok().build()
    }
}
