package ru.droptableusers.sampleapi.data.models.inout.output.users

data class AdminUserOutputResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val sex: String,
    val email: String,
    val birthdayDate: Long,
    val commandName: String,
    val docsReady: Boolean
)
