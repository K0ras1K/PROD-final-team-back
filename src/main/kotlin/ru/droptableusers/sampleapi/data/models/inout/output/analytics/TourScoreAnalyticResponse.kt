package ru.droptableusers.sampleapi.data.models.inout.output.analytics

import kotlinx.serialization.Serializable

@Serializable
data class TourScoreAnalyticResponse(
    val height: Float,
    val count: Int,
)
