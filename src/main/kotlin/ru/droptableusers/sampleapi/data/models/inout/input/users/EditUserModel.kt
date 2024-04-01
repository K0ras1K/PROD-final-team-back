package ru.droptableusers.sampleapi.data.models.inout.input.users

import kotlinx.serialization.Serializable

@Serializable
data class EditUserModel(
    val description: String,
    val tgLogin: String,
)
