package ch.bfh.habits.repositories

import ch.bfh.habits.entities.Group
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@RepositoryRestResource(collectionResourceRel = "groups", path = "groups")
@Repository
interface GroupDAO : PagingAndSortingRepository<Group, Long> {
    fun findAllByUserId(userId: Long): List<Group>
    fun findByUserIdAndId(userId: Long, id: Long): Group?
    fun deleteByIdAndUserId(id: Long, userId: Long)
}
