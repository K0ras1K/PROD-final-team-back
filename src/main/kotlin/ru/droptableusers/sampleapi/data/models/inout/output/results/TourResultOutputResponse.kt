package ru.droptableusers.sampleapi.data.models.inout.output.results

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.models.base.TourModel

@Serializable
data class TourResultOutputResponse(
    val tour: TourOutputResponse,
    val result: Float,
    val userId: Int
)