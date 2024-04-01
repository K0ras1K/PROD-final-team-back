package ru.droptableusers.sampleapi.data.models.inout.input.teams

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamRequest(
    val name: String,
    val description: String,
)
