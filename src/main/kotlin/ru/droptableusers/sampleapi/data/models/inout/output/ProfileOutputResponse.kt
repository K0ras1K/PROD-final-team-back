package ru.droptableusers.sampleapi.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class ProfileOutputResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    val tgId: String,
    val registerAt: Long
)
