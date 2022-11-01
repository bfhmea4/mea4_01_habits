package ch.bfh.habits.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class FizzBuzzTests {

    @Test
    fun one_returns_one_as_string() {
        assertThat(fizzbuzz(1)).isEqualTo("1")
    }

    @Test
    fun integer_not_divisible_by_three_or_five_returns_integer_as_string() {
        assertThat(fizzbuzz(2)).isEqualTo("2")
    }

    @Test
    fun three_returns_fizz() {
        assertThat(fizzbuzz(3)).isEqualTo("Fizz")
    }

    @Test
    fun integer_divisible_by_three_returns_fizz() {
        assertThat(fizzbuzz(9)).isEqualTo("Fizz")
    }

    @Test
    fun five_returns_buzz() {
        assertThat(fizzbuzz(5)).isEqualTo("Buzz")
    }

    @Test
    fun integer_divisible_by_five_returns_fizz() {
        assertThat(fizzbuzz(10)).isEqualTo("Buzz")
    }

    @Test
    fun integer_divisible_by_three_and_five_returns_fizzbuzz() {
        assertThat(fizzbuzz(15)).isEqualTo("FizzBuzz")
    }

}
