package ru.droptableusers.sampleapi.data.models.inout.output

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Group

@Serializable
data class ProfileOutputResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val group: Group,
    val tgId: String,
    val registerAt: Long,
    val description: String
)
