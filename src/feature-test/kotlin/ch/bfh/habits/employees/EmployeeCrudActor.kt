package ch.bfh.habits.employees

import ch.bfh.habits.dtos.EmployeeDTO
import ch.bfh.habits.dtos.EmployeeListDTO

interface EmployeeCrudActor {
    fun createsEmployee(employee: EmployeeDTO): Long
    fun seesEmployeeExists(employeeId: Long): Boolean
    fun getsEmployee(employeeId: Long): EmployeeDTO
    fun updatesEmployee(employeeId: Long, employee: EmployeeDTO)
    fun deletesEmployee(employeeId: Long)
    fun getsAllEmployees(): EmployeeListDTO
}
