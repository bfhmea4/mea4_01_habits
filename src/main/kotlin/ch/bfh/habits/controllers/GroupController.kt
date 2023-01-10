package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.entities.Group
import ch.bfh.habits.services.GroupService
import ch.bfh.habits.util.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class GroupController @Autowired constructor(private val service: GroupService, private val tokenProvider: TokenProvider) {
    @GetMapping("/api/groups")
    fun getAllGroups(@RequestHeader(value = "Authorization") token: String): ResponseEntity<List<Group>> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(service.getAllGroups(userId))
    }

    @PostMapping("/api/group", consumes = ["application/json"])
    fun newGroup(@RequestHeader(value = "Authorization") token: String, @RequestBody groupDTO: GroupDTO): ResponseEntity<Group> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.status(HttpStatus.CREATED).body(service.newGroup(groupDTO, userId))
    }

    @GetMapping("/api/group/{id}")
    fun getGroup(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<Group> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(service.getGroup(id, userId))
    }

    @DeleteMapping("/api/group/{id}")
    fun deleteGroup(@RequestHeader(value = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<Unit> {
        val userId = tokenProvider.extractId(token)
        service.deleteGroup(id, userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/api/group/{id}")
    fun updateGroup(@RequestHeader(value = "Authorization") token: String, @RequestBody group: GroupDTO, @PathVariable id: Long): ResponseEntity<Group> {
        val userId = tokenProvider.extractId(token)
        return ResponseEntity.ok().body(service.updateGroup(id, group, userId))
    }
}
