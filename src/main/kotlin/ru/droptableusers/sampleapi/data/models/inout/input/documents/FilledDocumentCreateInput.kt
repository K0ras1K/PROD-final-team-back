package ru.droptableusers.sampleapi.data.models.inout.input.documents

import kotlinx.serialization.Serializable

@Serializable
data class FilledDocumentCreateInput(
    val fileName: String,
    val documentId: Int
)
