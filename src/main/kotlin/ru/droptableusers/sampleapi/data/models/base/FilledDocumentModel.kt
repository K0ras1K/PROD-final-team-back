package ru.droptableusers.sampleapi.data.models.base

data class FilledDocumentModel(
    val id: Int,
    val userId: Int,
    val documentId: Int,
    val fileName: String,
    val lastUpdate: Long,
)
