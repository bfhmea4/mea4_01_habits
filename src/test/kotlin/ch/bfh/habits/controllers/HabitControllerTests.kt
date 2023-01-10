package ch.bfh.habits.controllers

import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.services.HabitService
import ch.bfh.habits.services.UserService
import ch.bfh.habits.util.TokenProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HabitController::class)
internal class HabitControllerTests {
    @TestConfiguration
    class HabitControllerTestsConfig {
        @Bean
        fun service() = mockk<HabitService>()
        @Bean
        fun tokenProvider() = mockk<TokenProvider> {
            every { validateToken(any(), any()) } returns true
            every { extractUsername("correctUser") } returns "correctUser"
            every { extractUsername("wrongUser") } returns "wrongUser"
            every { extractId("correctUser") } returns 1
            every { extractId("wrongUser") } returns 2
        }
        @Bean
        fun userService() = mockk<UserService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: HabitService

    private val mapper = jacksonObjectMapper()

    @Test
    @WithMockUser(username = "correctUser")
    fun `getAllHabits return the current users habits`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        every { service.getAllHabits(1) } returns arrayListOf(habit)
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        val result = mockMvc.perform(get("/api/habits").header("Authorization", userName))
            .andExpect(status().isOk)
            .andReturn()

        // then
        assertEquals(mapper.writeValueAsString(arrayListOf(habit)), result.response.contentAsString)
        verify { service.getAllHabits(1) }
    }

    @Test
    @WithMockUser(username = "correctUser")
    fun `getHabit return the requested habit`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        every { service.getHabit(1, 1) } returns habit
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        val result = mockMvc.perform(get("/api/habit/1").header("Authorization", userName))
            .andExpect(status().isOk)
            .andReturn()

        // then
        assertEquals(mapper.writeValueAsString(habit), result.response.contentAsString)
        verify { service.getHabit(1, 1) }
    }

    @Test
    @WithMockUser(username = "wrongUser")
    fun `getHabit returns not found if user is not allowed to access habit`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        every { service.getHabit(1, 1) } returns habit
        every { service.getHabit(1, 2) } throws EntityNotFoundException("")
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        mockMvc.perform(get("/api/habit/1").header("Authorization", userName))
            .andExpect(status().isNotFound)
            .andReturn()

        // then
        verify { service.getHabit(1, 2) }
    }

    @Test
    @WithMockUser(username = "correctUser")
    fun `getHabit returns not found if habit does not exist`() {
        // given
        val habit = Habit("Gym", "Go to the gym",1, userId = 1)
        every { service.getHabit(1, 1) } returns habit
        every { service.getHabit(2, 1) } throws EntityNotFoundException("")
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        mockMvc.perform(get("/api/habit/2").header("Authorization", userName))
            .andExpect(status().isNotFound)
            .andReturn()

        // then
        verify { service.getHabit(2, 1) }
    }
}
