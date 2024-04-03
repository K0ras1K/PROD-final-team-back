package ru.droptableusers.sampleapi.telegram.models.base

import ru.droptableusers.sampleapi.data.enums.InviteStatus

data class InviteBotModel(
    val from: String,
    val type: InviteStatus,
    val teamId: Int,
    val id: Int
)
