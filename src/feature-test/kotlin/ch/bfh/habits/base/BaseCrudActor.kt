package ch.bfh.habits.base

import ch.bfh.habits.dtos.user.JwtTokenDTO
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.entities.User

interface BaseCrudActor {
    fun login(input: LoginDTO): JwtTokenDTO
    fun register(input: RegisterDTO): User
}
