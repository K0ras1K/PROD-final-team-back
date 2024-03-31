package ru.droptableusers.sampleapi.data.models.inout.output.teams

import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse

data class InvitesRespondModel(
    val user: ProfileOutputResponse,
    val id: Int
)