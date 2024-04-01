package ru.droptableusers.sampleapi.data.models.inout.output.teams

import kotlinx.serialization.Serializable

@Serializable
class SmallTeamRespondModel(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val bannerUrl: String,
    val membersCount: Int,
)
