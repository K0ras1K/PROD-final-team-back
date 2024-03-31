package ru.droptableusers.sampleapi.data.models.base

data class UserModel(
    val username: String,
    val password: String,
    val tgLogin: String,
    val firstName: String,
    val lastName: String,
    val birthdayDate: Long,
    val regTime: Long,
)
