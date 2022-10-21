package ch.bfh.habits.services

import ch.bfh.habits.dtos.*
import ch.bfh.habits.repositories.EmployeeDAO
import ch.bfh.habits.entities.Employee
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeService(private val employeeDAO: EmployeeDAO) {

    @Transactional
    fun newEmployee(employeeDTO: EmployeeDTO): ObjectIdDTO {
        val newEmployee = createEmployeeEntityFromDTO(employeeDTO)
        employeeDAO.save(newEmployee)
        return ObjectIdDTO(newEmployee.id!!)
    }

    @Transactional
    fun getEmployeeById(id: Long): EmployeeDTO {
        val employee = employeeDAO.findById(id).orElseThrow {
            EntityNotFoundException("Employee id = $id not found")
        }
        return createEmployeeDtoFromEntity(employee)
    }

    @Transactional
    fun updateEmployeeById(id: Long, employeeDTO: EmployeeDTO) {
        val currentEmployee = employeeDAO.findById(id).orElseThrow {
            EntityNotFoundException("Employee id = $id not found")
        }
        EmployeeEntityBuilder.applyEmployeeDtoToEntity(employeeDTO, currentEmployee)
    }

    @Transactional
    fun deleteEmployeeById(id: Long) {
        try {
            employeeDAO.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("Employee id = $id not found")
        }
    }

    @Transactional
    fun getAllEmployees(): EmployeeListDTO {
        val employees =  ArrayList<EmployeeDTO>()
        employeeDAO.findAll().forEach { i -> employees.add(createEmployeeDtoFromEntity(i))}
        return EmployeeListDTO(employees = employees)
    }


    private fun createEmployeeDtoFromEntity(employee: Employee) =
        EmployeeDtoBuilder.createEmployeeDtoFromEntity(employee)

    private fun createEmployeeEntityFromDTO(employeeDTO: EmployeeDTO): Employee =
        EmployeeEntityBuilder.createEmployeeEntityFromDTO(employeeDTO)
}
