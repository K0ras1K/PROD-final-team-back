package ru.droptableusers.sampleapi.telegram.models.base

import ru.droptableusers.sampleapi.data.models.inout.output.users.ProfileOutputResponse

data class FullTeamBotModel(
    val teamId: Int,
    val users: List<ProfileOutputResponse>,
)
