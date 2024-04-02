package ru.droptableusers.sampleapi.data.models.inout.input.vacancies

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Major

@Serializable
data class VacancyGenerateInputModel (
    val vacancyTemplates: List<VacancyTagsByMajor>
)


@Serializable
data class VacancyTagsByMajor(
    val major: Major,
    val tagIds: List<Int>
)