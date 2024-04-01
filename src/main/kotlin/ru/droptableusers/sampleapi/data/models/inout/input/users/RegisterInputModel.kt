package ru.droptableusers.sampleapi.data.models.inout.input.users

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Major

@Serializable
data class RegisterInputModel(
    val username: String,
    val password: String,
    val tgLogin: String,
    val firstName: String,
    val lastName: String,
    val birthdayDate: Long,
    val major: Major,
)
