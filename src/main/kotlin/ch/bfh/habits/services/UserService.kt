package ch.bfh.habits.services

import ch.bfh.habits.entities.User
import ch.bfh.habits.repositories.UserDAO
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserDAO) {
    fun save(user: User): User {
        return this.userRepository.save(user)
    }

    fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)
    }

    fun findByUserName(userName: String): User? {
        return this.userRepository.findByUserName(userName)
    }

    fun getById(id: Long): User? {
        return this.userRepository.findById(id).orElse(null)
    }
}
