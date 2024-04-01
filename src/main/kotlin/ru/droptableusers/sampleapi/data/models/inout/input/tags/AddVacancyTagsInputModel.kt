package ru.droptableusers.sampleapi.data.models.inout.input.tags

data class AddVacancyTagsInputModel(
    val vacancyId: Int,
    val tagIdList: List<Int>
)
