package ch.bfh.habits.repositories

import ch.bfh.habits.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@Repository
interface UserDAO : JpaRepository<User, Long> {
    fun findByUserName(userName: String): User?
}
