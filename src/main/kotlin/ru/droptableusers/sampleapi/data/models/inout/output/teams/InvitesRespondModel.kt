package ru.droptableusers.sampleapi.data.models.inout.output.teams

import ru.droptableusers.sampleapi.data.models.inout.output.users.ProfileOutputResponse

data class InvitesRespondModel(
    val user: ProfileOutputResponse,
    val id: Int,
)
