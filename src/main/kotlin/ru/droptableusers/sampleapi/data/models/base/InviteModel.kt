package ru.droptableusers.sampleapi.data.models.base

import ru.droptableusers.sampleapi.data.enums.InviteStatus

data class InviteModel(
    val id: Int,
    val teamId: Int,
    val userId: Int,
    val type: InviteStatus,
)
