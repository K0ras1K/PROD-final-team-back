package ru.droptableusers.sampleapi.data.models.inout.output.users

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.enums.Major

@Serializable
data class ProfileOutputResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val group: Group,
    val tgId: String,
    val registerAt: Long,
    val description: String,
    val major: Major,
    val team: Int,
)
