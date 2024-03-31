package ru.droptableusers.sampleapi.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class ProfileOutputResponse(
    val username: String,
    val password: String,
    val registerAt: String,
)
