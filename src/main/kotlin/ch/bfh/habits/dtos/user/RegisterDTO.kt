package ch.bfh.habits.dtos.user

data class RegisterDTO (
    val password: String,
    val email: String,
    val userName: String,
    val firstName: String,
    val lastName: String? = null
)
