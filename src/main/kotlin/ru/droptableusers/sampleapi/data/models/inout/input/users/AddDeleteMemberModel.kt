package ru.droptableusers.sampleapi.data.models.inout.input.users

import kotlinx.serialization.Serializable

@Serializable
data class AddDeleteMemberModel (
    val userId: Int,
    val teamId: Int
)