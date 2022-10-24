package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.services.HabitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HabitController @Autowired constructor(private val service: HabitService) {
    @GetMapping("/api/habits")
    fun getAllHabits(): ResponseEntity<HabitListDTO> {
        return ResponseEntity.ok().body(service.getAllHabits())
    }
}
