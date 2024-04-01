package ru.droptableusers.sampleapi.data.models.inout.output.results

import kotlinx.serialization.Serializable

@Serializable
data class TourResultOutputResponse(
    val tour: TourOutputResponse,
    val result: Float,
    val userId: Int,
)
