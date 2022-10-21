package ch.bfh.habits.dtos

import ch.bfh.habits.entities.Employee

class EmployeeEntityBuilder private constructor() {

    companion object {
        fun createEmployeeEntityFromDTO(employeeDTO: EmployeeDTO): Employee {
            return Employee(
                name = employeeDTO.name ?: ""
            )
        }

        fun applyEmployeeDtoToEntity(employeeDTO: EmployeeDTO, employeeEntity: Employee) {
            employeeEntity.name = employeeDTO.name ?: employeeEntity.name
        }
    }

}
