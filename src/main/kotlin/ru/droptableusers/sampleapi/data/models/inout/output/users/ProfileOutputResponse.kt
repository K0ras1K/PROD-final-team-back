package ru.droptableusers.sampleapi.data.models.inout.output.users

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.enums.Major
import ru.droptableusers.sampleapi.data.models.base.TagModel

@Serializable
data class ProfileOutputResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val group: Group,
    val tgLogin: String,
    val registerAt: Long,
    val description: String,
    val major: Major?,
    val team: Int,
    val tags: List<TagModel>
)
