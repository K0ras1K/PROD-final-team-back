package ru.droptableusers.sampleapi.data.models.inout.input.groups

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Group

@Serializable
class GroupSetRequestModel(
    val userId: Int,
    val group: Group,
)
