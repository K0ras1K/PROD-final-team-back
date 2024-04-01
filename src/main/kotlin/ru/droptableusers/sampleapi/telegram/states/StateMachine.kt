package ru.droptableusers.sampleapi.telegram.states

import ru.droptableusers.sampleapi.telegram.models.StatusData
import ru.droptableusers.sampleapi.utils.Logger

object StateMachine {
    fun setStatus(
        userId: Long,
        statusData: StatusData,
    ) {
        Caching.status_cache[userId] = statusData
    }

    fun getStatus(userId: Long): StatusData? {
        return if (Caching.status_cache.containsKey(userId)) {
            Logger.logger.info("Returning from local cache")
            Caching.status_cache[userId]
        } else {
            null
        }
    }

    fun removeStatus(userId: Long) {
        Caching.status_cache.remove(userId)
    }
}
