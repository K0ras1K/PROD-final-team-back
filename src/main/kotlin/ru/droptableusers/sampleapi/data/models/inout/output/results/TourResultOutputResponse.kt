package ru.droptableusers.sampleapi.data.models.inout.output.results

import kotlinx.serialization.Serializable

@Serializable
data class TourResultOutputResponse(
    val tourName: String,
    val result: Float
)