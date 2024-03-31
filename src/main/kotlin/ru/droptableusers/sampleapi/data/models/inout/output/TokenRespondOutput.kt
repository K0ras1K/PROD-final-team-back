package ru.droptableusers.sampleapi.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class TokenRespondOutput(
    val token: String,
)
