package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.EmployeeDTO
import ch.bfh.habits.dtos.EmployeeListDTO
import ch.bfh.habits.services.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class EmployeeController @Autowired constructor(private val service: EmployeeService) {

    @GetMapping("/api/employees")
    fun getAllEmployees(): ResponseEntity<EmployeeListDTO> {
        return ResponseEntity.ok().body(service.getAllEmployees())
    }

    @GetMapping("/api/employees/{id}")
    fun getEmployee(@PathVariable id: Long): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok().body(service.getEmployeeById(id))
    }

    @PostMapping("/api/employees")
    fun newEmployee(@RequestBody employeeDTO: EmployeeDTO): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.newEmployee(employeeDTO))
    }

    @PutMapping("/api/employees/{id}")
    fun updateEmployee(@RequestBody employee: EmployeeDTO, @PathVariable id: Long): ResponseEntity<Any> {
        service.updateEmployeeById(id, employee)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/api/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long): ResponseEntity<Any> {
        service.deleteEmployeeById(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
