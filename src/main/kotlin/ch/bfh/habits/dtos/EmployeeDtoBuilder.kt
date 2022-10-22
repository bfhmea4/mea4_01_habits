package ch.bfh.habits.dtos

import ch.bfh.habits.entities.Employee

class EmployeeDtoBuilder private constructor() {

    companion object {
        fun createEmployeeDtoFromEntity(employeeEntity: Employee): EmployeeDTO {
            return EmployeeDTO(
                name = employeeEntity.name,
                id = employeeEntity.id
            )
        }
    }
}
