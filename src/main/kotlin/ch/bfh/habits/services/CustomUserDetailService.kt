package ch.bfh.habits.services

import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.UserDAO
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(private val userDAO: UserDAO) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userDAO.findByUserName(username) ?: throw EntityNotFoundException("User not found")
        return User(user.userName, user.password, emptyList())
    }
}
