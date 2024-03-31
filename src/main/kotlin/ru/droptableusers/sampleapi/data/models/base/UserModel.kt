package ru.droptableusers.sampleapi.data.models.base

data class UserModel(
    val id: Int,
    val username: String,
    var password: String,
    var tgLogin: String,
    val firstName: String,
    val lastName: String,
    val birthdayDate: Long,
    val regTime: Long,
    var description: String
)
