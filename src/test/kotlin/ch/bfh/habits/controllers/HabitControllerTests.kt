package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.services.HabitService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@ExtendWith(SpringExtension::class)
@WebMvcTest(HabitController::class)
internal class HabitControllerTests {

    @TestConfiguration
    class HabitControllerTestsConfig {
        @Bean
        fun service() = mockk<HabitService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: HabitService

    @Test
    fun controller_invokes_getAllHabits_function() {
        val habit = HabitDTO(1, "Gym", "Go to the Gym more often", false)

        every { service.getAllHabits() } returns HabitListDTO(arrayListOf(habit))

        val result = mockMvc.perform(get("/api/habits"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        val mapper = jacksonObjectMapper()
        assertEquals(mapper.writeValueAsString(HabitListDTO(arrayListOf(habit))), result.response.contentAsString)
        verify { service.getAllHabits() }
    }
}
