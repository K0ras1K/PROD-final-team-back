package ru.droptableusers.sampleapi.data.models.inout.output.analytics

import kotlinx.serialization.Serializable

@Serializable
data class TourAnalyticResponse(
    val name: String,
    val averageMedian: Float,
    val averageAlgebraic: Float,
    val users: List<TourScoreAnalyticResponse>,
)
