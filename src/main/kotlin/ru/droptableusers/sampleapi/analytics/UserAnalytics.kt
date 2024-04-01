package ru.droptableusers.sampleapi.analytics

import ru.droptableusers.sampleapi.database.persistence.ToursPersistence

object UserAnalytics {
    /**
     * @return percent
     *
     * @author SoraVWV
     */
    fun handleAverageAlgebraic(userId: Int): Float {
        val tourResultModels = ToursPersistence().selectResultsByUserId(userId)
        val results = tourResultModels.map { it.result }

        return AnalyticsUtil.averageAlgebraic(results)
    }

    /**
     * @return percent
     *
     * @author SoraVWV
     */
    fun handleAverageMedian(userId: Int): Float {
        val tourResultModels = ToursPersistence().selectResultsByUserId(userId)
        val results = tourResultModels.map { it.result }

        return AnalyticsUtil.averageMedian(results)
    }
}
