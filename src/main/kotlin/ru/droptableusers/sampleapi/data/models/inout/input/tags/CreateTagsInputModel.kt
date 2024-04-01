package ru.droptableusers.sampleapi.data.models.inout.input.tags

import kotlinx.serialization.Serializable

@Serializable
data class CreateTagsInputModel(
    val tagList: List<String>,
)
