package ch.bfh.habits.dtos.user

import ch.bfh.habits.entities.User

class UserEntityBuilder private constructor() {

    companion object {
        fun createUserEntityFromDTO(registerDTO: RegisterDTO): User {
            val user = User(
                userName = registerDTO.userName,
                email = registerDTO.email,
                firstName = registerDTO.firstName,
                lastName = registerDTO.lastName ?: ""
            )
            user.password = registerDTO.password
            return user
        }
    }
}
