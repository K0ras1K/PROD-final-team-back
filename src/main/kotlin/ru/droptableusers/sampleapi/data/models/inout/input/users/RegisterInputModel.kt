package ru.droptableusers.sampleapi.data.models.inout.input.users

import kotlinx.serialization.Serializable

@Serializable
data class RegisterInputModel(
    val username: String,
    val password: String,
    val tgLogin: String,
    val firstName: String,
    val lastName: String,
    val birthdayDate: Long,
)
