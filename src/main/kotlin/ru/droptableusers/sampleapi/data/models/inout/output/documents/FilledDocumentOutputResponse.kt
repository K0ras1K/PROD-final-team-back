package ru.droptableusers.sampleapi.data.models.inout.output.documents

import kotlinx.serialization.Serializable

@Serializable
data class FilledDocumentOutputResponse(
    val id: Int,
    val fileName: String,
    val userId: Int,
    val documentId: Int,
    val lastUpdate: Long
)
