package ru.droptableusers.sampleapi.telegram.states

import ru.droptableusers.sampleapi.telegram.models.StatusData

object Caching {
    val status_cache: MutableMap<Long, StatusData> = mutableMapOf()
}
