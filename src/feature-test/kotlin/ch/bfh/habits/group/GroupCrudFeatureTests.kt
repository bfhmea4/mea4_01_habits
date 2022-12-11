package ch.bfh.habits.group

import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.Group
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GroupCrudFeatureTests {
    @Autowired
    private lateinit var groupActor: GroupCrudActor

    @Nested
    @DisplayName("Given no groups exist THEN we")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    inner class GivenNoGroupExists {
        @BeforeAll
        fun setup() {
            groupActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            groupActor.login(LoginDTO("test", "test"))
        }

        @Test
        fun `get an empty list for get all`() {
            assertThat(groupActor.getsAllGroups()).isEqualTo(arrayListOf<Group>())
        }
    }

    @Nested
    @DisplayName("Given we have created a group THEN we ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @TestMethodOrder(OrderAnnotation::class)
    inner class GivenNewHabitCreated {
        private lateinit var group: Group

        @BeforeAll
        fun setup() {
            groupActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            groupActor.login(LoginDTO("test", "test"))
            group = groupActor.createsGroup(createGroupDTO())
        }

        @Test
        @Order(1)
        fun `can find it amongst all groups`() {
            // when
            groupActor.createsGroup(createGroupDTO("BFH"))
            val allGroups = groupActor.getsAllGroups()

            // then
            assertThat(allGroups.size).isGreaterThan(1)
            assertThat(allGroups.filter { i -> i.id == group.id }).isNotEmpty
        }

        @Test
        @Order(2)
        fun `can find it`() {
            assertThat(groupActor.seesGroupExists(group.id!!)).isTrue
        }

        @Test
        @Order(3)
        fun `can get it`() {
            assertThat(groupActor.getsGroup(group.id!!).id).isEqualTo(group.id)
        }

        @Test
        @Order(4)
        fun `can update it`() {
            // given
            val newHabit = createGroupDTO("New")
            // when
            groupActor.updatesGroup(group.id!!, newHabit)
            // then
            val updatedHabit = groupActor.getsGroup(group.id!!)
            assertThat(updatedHabit.title).isEqualTo("New")
        }

        @Test
        @Order(5)
        fun `can delete it`() {
            groupActor.deletesGroup(group.id!!)
            assertThat(groupActor.seesGroupExists(group.id!!)).isFalse
        }
    }

    @Nested
    @DisplayName("Given a non-existing group id THEN ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    inner class GivenNonExistingHabit {
        @BeforeAll
        fun setup() {
            groupActor.register(RegisterDTO("test", "test@test.com", "test", "Test"))
            groupActor.login(LoginDTO("test", "test"))
        }

        private val nonExistingGroupId = -1L

        @Test
        fun `find returns false`() {
            assertThat(groupActor.seesGroupExists(nonExistingGroupId)).isFalse
        }

        @Test
        fun `get throws not found`() {
            assertThrows<EntityNotFoundException> {
                groupActor.getsGroup(nonExistingGroupId)
            }
        }

        @Test
        fun `delete throws not found`() {
            assertThrows<EntityNotFoundException> {
                groupActor.deletesGroup(nonExistingGroupId)
            }
        }

        @Test
        fun `update throws not found`() {
            assertThrows<EntityNotFoundException> {
                groupActor.updatesGroup(nonExistingGroupId, createGroupDTO())
            }
        }
    }

    private fun createGroupDTO(title: String = "Private") = GroupDTO(title = title)
}
