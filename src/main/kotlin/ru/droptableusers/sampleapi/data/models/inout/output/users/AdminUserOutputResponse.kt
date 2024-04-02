package ru.droptableusers.sampleapi.data.models.inout.output.users

import kotlinx.serialization.Serializable

@Serializable
data class AdminUserOutputResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val sex: String,
    val email: String,
    val birthdayDate: Long,
    val commandName: String,
    val filledDocs: Int,
    val requiredDocs: Int
)
