package ru.droptableusers.sampleapi.data.models.inout.input.teams

import kotlinx.serialization.Serializable

@Serializable
data class TagsTeamRequest(
    val tags: List<Int>,
)
