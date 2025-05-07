package it.marcointroini.model

data class AppUser(
    val email: String,
    val password: String,
    val type: UserType,
    val firstName: String,
    val lastName: String,
)

enum class UserType {
    ADMIN, USER, SUPPORT
}
