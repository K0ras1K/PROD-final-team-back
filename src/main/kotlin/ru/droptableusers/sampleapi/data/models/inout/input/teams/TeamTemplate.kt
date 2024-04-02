package ru.droptableusers.sampleapi.data.models.inout.input.teams

import kotlinx.serialization.Serializable
import ru.droptableusers.sampleapi.data.enums.Major

@Serializable
data class TeamTemplate(val slots: List<Slot>)

@Serializable
data class Slot(
    val possibleItems: Set<Major>,
    val required: Boolean,
)
