package ru.droptableusers.sampleapi.data.models.base

import kotlinx.serialization.Serializable

@Serializable
data class TagModel(
    val id: Int,
    val tagString: String,
)
