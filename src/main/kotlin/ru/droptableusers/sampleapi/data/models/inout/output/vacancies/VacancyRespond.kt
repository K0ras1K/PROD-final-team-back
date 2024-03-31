package ru.droptableusers.sampleapi.data.models.inout.output.vacancies

import kotlinx.serialization.Serializable

@Serializable
data class VacancyRespond(
    val teamId: Int,
    val tags: List<String>
)
