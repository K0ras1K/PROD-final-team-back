package ru.droptableusers.sampleapi.data.models.inout.output.teams

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.models.inout.output.users.ProfileOutputResponse

@Serializable
data class TeamRespondModel(
    val team: SmallTeamRespondModel,
    val users: List<ProfileOutputResponse>,
)
