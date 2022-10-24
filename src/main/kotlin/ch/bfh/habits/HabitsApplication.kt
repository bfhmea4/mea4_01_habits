package ch.bfh.habits

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class HabitsApplication

fun main(args: Array<String>) {
    runApplication<HabitsApplication>(*args)
}
