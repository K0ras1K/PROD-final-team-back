package ru.droptableusers.sampleapi.data.models.inout.input.documents

import kotlinx.serialization.Serializable

@Serializable
data class DocumentConditionUpdateInput(
    val id: Int,
    val fieldName: String,
    val condition: String,
    val value: String,
)
