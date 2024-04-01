package ru.droptableusers.sampleapi.data.models.inout.input.tags

import kotlinx.serialization.Serializable

@Serializable
data class AddUserTagsInputModel(
    val userId: Int,
    val tagIdList: List<Int>,
)
