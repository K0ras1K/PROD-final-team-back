package ru.droptableusers.sampleapi.data.models.base

data class DocumentModel(
    val id: Int,
    val name: String,
    val description: String,
    val required: Boolean,
    val template: String,
    val extensions: String,
)
