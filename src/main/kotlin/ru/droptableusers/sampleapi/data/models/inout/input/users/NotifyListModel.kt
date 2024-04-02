package ru.droptableusers.sampleapi.data.models.inout.input.users

import kotlinx.serialization.Serializable

@Serializable
data class NotifyListModel(
    val userIds: List<Int>,
    val message: String
)
