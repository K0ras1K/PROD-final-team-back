package ru.droptableusers.sampleapi.data.models.base

data class DocumentConditionModel(
    val id: Int,
    val documentId: Int,
    val fieldName: String,
    val value: String,
    val condition: String,
)
