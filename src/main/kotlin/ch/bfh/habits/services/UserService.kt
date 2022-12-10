package ch.bfh.habits.services

import ch.bfh.habits.entities.User
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.UserDAO
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as SpringUser

@Service
class UserService(private val userDAO: UserDAO): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userDAO.findByUserName(username) ?: throw EntityNotFoundException("User not found")
        return SpringUser(user.userName, user.password, emptyList())
    }

    fun save(user: User): User {
        return this.userDAO.save(user)
    }

    fun findByUserName(userName: String): User? {
        return this.userDAO.findByUserName(userName)
    }
}
