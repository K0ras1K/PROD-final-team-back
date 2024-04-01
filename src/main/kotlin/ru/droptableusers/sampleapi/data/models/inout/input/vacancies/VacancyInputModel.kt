package ru.droptableusers.sampleapi.data.models.inout.input.vacancies

import kotlinx.serialization.Serializable

@Serializable
data class VacancyInputModel(
    val teamId: Int,
    val tagList: List<Int>,
)
