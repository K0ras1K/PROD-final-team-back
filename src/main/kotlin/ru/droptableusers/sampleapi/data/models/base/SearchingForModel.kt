package ru.droptableusers.sampleapi.data.models.base

import ru.droptableusers.sampleapi.data.enums.Major

data class SearchingForModel(
    val id: Int,
    val teamId: Int,
    val slotIndex: Int,
    val major: Major
)
