package ch.bfh.habits.employees

import ch.bfh.habits.controllers.EmployeeController
import ch.bfh.habits.employees.impl.WebClientBasedEmployeeCrudActor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class EmployeeTestConfiguration {

    @Value("\${habits.test.localhost:false}")
    private var localhost: Boolean = false

    @Bean
    fun actor(controller: EmployeeController): EmployeeCrudActor {
        val client =  if (localhost) {
            WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
        } else {
            WebTestClient.bindToController(controller).configureClient().build()
        }
        return WebClientBasedEmployeeCrudActor(client)
    }

}
