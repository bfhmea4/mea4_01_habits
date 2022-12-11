package ch.bfh.habits.controllers

import ch.bfh.habits.entities.Group
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.services.GroupService
import ch.bfh.habits.services.UserService
import ch.bfh.habits.util.TokenProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(GroupController::class)
internal class GroupControllerTests {
    @TestConfiguration
    class GroupControllerTestsConfig {
        @Bean
        fun service() = mockk<GroupService>()
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
    private lateinit var service: GroupService

    private val mapper = jacksonObjectMapper()

    @Test
    @WithMockUser(username = "correctUser")
    fun `getAllGroups return the current users groups`() {
        // given
        val group = Group("Private",1, userId = 1)
        every { service.getAllGroups(1) } returns arrayListOf(group)
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/groups").header("Authorization", userName))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // then
        Assertions.assertEquals(mapper.writeValueAsString(arrayListOf(group)), result.response.contentAsString)
        verify { service.getAllGroups(1) }
    }

    @Test
    @WithMockUser(username = "correctUser")
    fun `getHabit return the requested group`() {
        // given
        val group = Group("Private",1, userId = 1)
        every { service.getGroup(1, 1) } returns group
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/group/1").header("Authorization", userName))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // then
        Assertions.assertEquals(mapper.writeValueAsString(group), result.response.contentAsString)
        verify { service.getGroup(1, 1) }
    }

    @Test
    @WithMockUser(username = "wrongUser")
    fun `getGroup returns not found if user is not allowed to access group`() {
        // given
        val group = Group("Private", 1, userId = 1)
        every { service.getGroup(1, 1) } returns group
        every { service.getGroup(1, 2) } throws EntityNotFoundException("")
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/1").header("Authorization", userName))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()

        // then
        verify { service.getGroup(1, 2) }
    }

    @Test
    @WithMockUser(username = "correctUser")
    fun `getGroup returns not found if group does not exist`() {
        // given
        val group = Group("Private", 1, userId = 1)
        every { service.getGroup(1, 1) } returns group
        every { service.getGroup(2, 1) } throws EntityNotFoundException("")
        val userName = SecurityContextHolder.getContext().authentication.name

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/2").header("Authorization", userName))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()

        // then
        verify { service.getGroup(2, 1) }
    }
}
