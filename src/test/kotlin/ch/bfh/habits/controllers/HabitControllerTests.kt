package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.ObjectIdDTO
import ch.bfh.habits.dtos.habit.HabitDTO
import ch.bfh.habits.dtos.habit.HabitListDTO
import ch.bfh.habits.exceptions.EntityNotFoundException
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
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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

    private val mapper = jacksonObjectMapper()

    @Test
    fun controller_invokes_getAllHabits_function() {
        val habit = HabitDTO("Gym", "Go to the Gym more often", false, 1)

        every { service.getAllHabits() } returns HabitListDTO(arrayListOf(habit))

        val result = mockMvc.perform(get("/api/habits"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(HabitListDTO(arrayListOf(habit))), result.response.contentAsString)
        verify { service.getAllHabits() }
    }

    @Test
    fun controller_invokes_newHabit_function() {
        val habit = HabitDTO("Gym", "Go to the Gym more often", false)

        every { service.newHabit(any()) } returns ObjectIdDTO(1)

        val result = mockMvc.perform(post("/api/habit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(habit)))
            .andExpect(status().isCreated)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(ObjectIdDTO(1)), result.response.contentAsString)
        verify { service.newHabit(any()) }
    }

    @Test
    fun controller_invokes_getHabit_function() {
        val habit = HabitDTO("Gym", "Go to the Gym more often", false, 1)

        every { service.getHabitById(1) } returns habit

        val result = mockMvc.perform(get("/api/habit/1"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(habit), result.response.contentAsString)
        verify { service.getHabitById(1) }
    }

    @Test
    fun controller_invokes_deleteHabit_function() {
        every { service.deleteHabitById(1) } returns Unit

        mockMvc.perform(delete("/api/habit/1"))
            .andExpect(status().isNoContent)

        verify { service.deleteHabitById(1) }
    }

    @Test
    fun controller_invokes_deleteHabit_function_on_non_existent_id() {
        every { service.deleteHabitById(2) } throws EntityNotFoundException("Habit not found")

        mockMvc.perform(delete("/api/habit/2"))
            .andExpect(status().is4xxClientError)

        verify { service.deleteHabitById(2) }
    }

    @Test
    fun controller_invokes_updateHabit_function() {
        val habit = HabitDTO("Gym_Old", "Go to the Gym more often", true)
        val habitReturned = HabitDTO("Gym_Old", "Go to the Gym more often", true, 1)

        every { service.updateHabitById(1, any()) } returns Unit
        every { service.getHabitById(1) } returns habitReturned

        mockMvc.perform(put("/api/habit/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(habit)))
            .andExpect(status().isOk)

        val result = mockMvc.perform(get("/api/habit/1"))
            .andExpect(status().isOk)
            .andDo(print())
            .andReturn()

        assertEquals(mapper.writeValueAsString(habitReturned), result.response.contentAsString)
        verify { service.updateHabitById(1, any()) }
        verify { service.getHabitById(1) }
    }
}
