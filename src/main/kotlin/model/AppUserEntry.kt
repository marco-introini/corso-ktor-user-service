package it.marcointroini.model

import java.util.UUID

data class AppUserEntry(
    val uuid: UUID,
    val email: String,
    val type: UserType,
    val firstName: String,
    val lastName: String,
)
