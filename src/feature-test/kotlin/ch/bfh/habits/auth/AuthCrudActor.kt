package ch.bfh.habits.auth

import ch.bfh.habits.dtos.user.JwtTokenDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.User

interface AuthCrudActor {
    fun login(input: LoginDTO): JwtTokenDTO
    fun register(input: RegisterDTO): User
}
