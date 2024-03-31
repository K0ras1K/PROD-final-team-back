package ru.droptableusers.sampleapi.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String,
)
