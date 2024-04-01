package ru.droptableusers.sampleapi.data.models.inout.output.tags

import kotlinx.serialization.Serializable

@Serializable
data class TagsOutput(val list: List<TagObjectOutput>)

@Serializable
data class TagObjectOutput(
    val id: Int,
    val tag: String,
)
