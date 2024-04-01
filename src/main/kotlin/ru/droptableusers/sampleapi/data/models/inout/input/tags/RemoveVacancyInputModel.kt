package ru.droptableusers.sampleapi.data.models.inout.input.tags

import kotlinx.serialization.Serializable

@Serializable
data class RemoveVacancyInputModel(
    val vacancyId: Int,
    val tagId: Int,
)
