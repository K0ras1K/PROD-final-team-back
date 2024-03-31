package ru.droptableusers.sampleapi.data.models.inout.input.users

import kotlinx.serialization.Serializable

@Serializable
data class LoginInputModel(
    val username: String,
    val password: String,
)
