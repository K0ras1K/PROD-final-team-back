package ru.droptableusers.sampleapi.data.models.inout.output.users

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel

@Serializable
data class UserInvitesRespondModel(
    val team: SmallTeamRespondModel,
    val id: Int,
)
