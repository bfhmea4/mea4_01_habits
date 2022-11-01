package ch.bfh.habits.fizzbuzz

import ch.bfh.habits.fizzbuzz.impl.WebClientBasedFizzBuzzActor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FizzBuzzTestConfiguration {

    @Value("\${habits.test.localhost:false}")
    private var localhost: Boolean = false

    @Bean
    fun fizzBuzzActor(): FizzBuzzActor {
        return if (localhost) {
            WebClientBasedFizzBuzzActor.localHostServer()
        } else {
            WebClientBasedFizzBuzzActor.mockServer()
        }
    }
}
