package ru.droptableusers.sampleapi.data.models.inout.output.analytics

import kotlinx.serialization.Serializable

@Serializable
data class UserTourAnalyticResponse(
    val name: String,
    val score: Float,
    val maxScore: Short,
)
