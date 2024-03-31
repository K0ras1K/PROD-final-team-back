package ru.droptableusers.sampleapi.data.models.base

import kotlinx.serialization.Serializable

@Serializable
data class TeamModel(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val bannerUrl: String
)