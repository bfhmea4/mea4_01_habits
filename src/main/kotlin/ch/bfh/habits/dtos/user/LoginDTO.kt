package ch.bfh.habits.dtos.user

data class LoginDTO (
    val password: String,
    val email: String? = null,
    val userName: String? = null
)
