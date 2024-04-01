package ru.droptableusers.sampleapi.data.models.inout.input.tags

import kotlinx.serialization.Serializable

@Serializable
data class RemoveUserInputModel(
    val userId: Int,
    val tagId: Int,
)
