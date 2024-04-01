package ru.droptableusers.sampleapi.data.models.inout.output.documents

import kotlinx.serialization.Serializable

@Serializable
data class DocumentConditionOutputResponse(
    val id: Int,
    val fieldName: String,
    val condition: String,
    val value: String,
)
