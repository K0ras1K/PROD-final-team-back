package ru.droptableusers.sampleapi.data.models.inout.input.users

data class EditUserPassword(
    val oldPassword: String,
    val newPassword: String,
)
