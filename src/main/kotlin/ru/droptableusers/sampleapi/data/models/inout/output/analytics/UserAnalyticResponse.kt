package ru.droptableusers.sampleapi.data.models.inout.output.analytics

import kotlinx.serialization.Serializable

@Serializable
data class UserAnalyticResponse(
    val tours: List<UserTourAnalyticResponse>,
    val averageMedian: Float,
    val averageAlgebraic: Float,
)
