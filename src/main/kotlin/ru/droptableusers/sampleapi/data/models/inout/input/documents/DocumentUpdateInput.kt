package ru.droptableusers.sampleapi.data.models.inout.input.documents

import kotlinx.serialization.Serializable

@Serializable
data class DocumentUpdateInput(
    val id: Int,
    val name: String,
    val description: String,
    val required: Boolean,
    val template: String,
    val extensions: List<String>,
    val conditions: List<DocumentConditionUpdateInput>,
)
