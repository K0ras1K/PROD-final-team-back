package ru.droptableusers.sampleapi.data.models.base

import ru.droptableusers.sampleapi.data.enums.Group

data class GroupModel(
    val username: String,
    val group: Group
)