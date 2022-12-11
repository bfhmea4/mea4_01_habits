package ch.bfh.habits.repositories

import ch.bfh.habits.entities.Habit
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@RepositoryRestResource(collectionResourceRel = "habits", path = "habits")
@Repository
interface HabitDAO : PagingAndSortingRepository<Habit, Long> {
    fun findAllByGroupIdAndUserId(groupId: Long, userId: Long): List<Habit>
    fun findAllByUserId(userId: Long): List<Habit>
    fun findByUserIdAndId(userId: Long, id: Long): Habit?
}
