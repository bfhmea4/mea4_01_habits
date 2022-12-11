package ch.bfh.habits.group

import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.Group
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
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
            Assertions.assertThat(groupActor.getsAllGroups()).isEqualTo(arrayListOf<Group>())
        }
    }

    @Nested
    @DisplayName("Given we have created a group THEN we ...")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
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
            val allHabits = groupActor.getsAllGroups()

            // then
            Assertions.assertThat(allHabits.size).isGreaterThan(1)
            Assertions.assertThat(allHabits.filter { i -> i.id == group.id }).isNotEmpty
        }

        @Test
        @Order(2)
        fun `can find it`() {
            Assertions.assertThat(groupActor.seesGroupExists(group.id!!)).isTrue
        }

        @Test
        @Order(3)
        fun `can get it`() {
            Assertions.assertThat(groupActor.getsGroup(group.id!!).id).isEqualTo(group.id)
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
            Assertions.assertThat(updatedHabit.title).isEqualTo("New")
        }

        @Test
        @Order(5)
        fun `can delete it`() {
            groupActor.deletesGroup(group.id!!)
            Assertions.assertThat(groupActor.seesGroupExists(group.id!!)).isFalse
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
            Assertions.assertThat(groupActor.seesGroupExists(nonExistingGroupId)).isFalse
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
