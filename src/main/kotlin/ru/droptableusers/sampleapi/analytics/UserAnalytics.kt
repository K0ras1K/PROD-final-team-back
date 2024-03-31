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

    /**
     * @return tour-name = percent
     *
     * @author SoraVWV
     */
    fun handleGraph(userId: Int): Map<String, Float> {
        val tourResultModels = ToursPersistence().selectResultsByUserId(userId)
        val result = mutableMapOf<String, Float>()

        for (tourResultModel in tourResultModels) {
            val tourModel = ToursPersistence().selectTourById(tourResultModels[0].tourId) ?: continue
            result[tourModel.name] = tourResultModel.result / tourModel.maxScore
        }

        return result
    }

}