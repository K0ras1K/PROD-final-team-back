package ru.droptableusers.sampleapi.data.models.inout.output.teams

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse

@Serializable
data class TeamRespondModel(
    val team: TeamModel,
    val users: List<ProfileOutputResponse>
)