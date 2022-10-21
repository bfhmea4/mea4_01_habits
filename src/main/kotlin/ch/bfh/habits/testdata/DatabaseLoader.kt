package ch.bfh.habits.testdata

import ch.bfh.habits.entities.Employee
import ch.bfh.habits.entities.Activity
import ch.bfh.habits.repositories.EmployeeDAO
import ch.bfh.habits.repositories.ActivityDAO
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseLoader(
    private val employeeDAO: EmployeeDAO,
    private val activityDAO: ActivityDAO,
) : CommandLineRunner  {

    override fun run(vararg args: String?) {

        // Frodo
        val bilbo = Employee(name = "Frodo Baggins")
        val workUnit = Activity(performedBy = bilbo, description = "carried the ring to Mount Doom")
        employeeDAO.save(bilbo)
        activityDAO.save(workUnit)
    }

}
