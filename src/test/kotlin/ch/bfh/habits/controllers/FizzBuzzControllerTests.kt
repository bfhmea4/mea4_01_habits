package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.FizzBuzzResultDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity


class FizzBuzzControllerTests {

    @Test
    fun controller_invokes_fizzbuzz_function() {
        val result = FizzBuzzController().getFizzBuzzResult(6)
        val expectedResult = ResponseEntity.ok().body(
            FizzBuzzResultDTO(n = 6, result = "Fizz")
        )
        assertThat(result).isEqualTo(expectedResult)
    }

}
