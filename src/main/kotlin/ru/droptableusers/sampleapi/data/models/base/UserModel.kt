package ru.droptableusers.sampleapi.data.models.base

import ru.droptableusers.sampleapi.data.enums.Major

data class UserModel(
    val id: Int,
    val username: String,
    var password: String,
    var tgLogin: String,
    val firstName: String,
    val lastName: String,
    val birthdayDate: Long,
    val regTime: Long,
    val major: Major?,
    var description: String,
)
