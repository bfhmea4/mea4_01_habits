package ch.bfh.habits.repositories

import ch.bfh.habits.entities.Employee
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDAO : PagingAndSortingRepository<Employee, Long>
