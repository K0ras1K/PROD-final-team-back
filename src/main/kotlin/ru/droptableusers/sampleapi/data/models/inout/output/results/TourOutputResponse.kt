package ru.droptableusers.sampleapi.data.models.inout.output.results

import kotlinx.serialization.Serializable

@Serializable
data class TourOutputResponse(
    val name: String,
    val year: Short,
    val maxScore: Short,
)
