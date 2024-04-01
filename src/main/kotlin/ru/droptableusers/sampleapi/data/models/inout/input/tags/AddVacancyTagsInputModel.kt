package ru.droptableusers.sampleapi.data.models.inout.input.tags

import kotlinx.serialization.Serializable

@Serializable
data class AddVacancyTagsInputModel(
    val vacancyId: Int,
    val tagIdList: List<Int>,
)
