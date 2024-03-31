package ru.droptableusers.sampleapi.analytics

import ru.droptableusers.sampleapi.database.persistence.ToursPersistence

object TourAnalytics {

    /**
     * @return percent
     *
     * @author SoraVWV
     */
    fun handleAverageAlgebraic(tourId: Int): Float {
        val tourResultModels = ToursPersistence().selectResultsByTourId(tourId)
        val results = tourResultModels.map { it.result }

        return AnalyticsUtil.averageAlgebraic(results)
    }

    /**
     * @return percent
     *
     * @author SoraVWV
     */
    fun handleAverageMedian(tourId: Int): Float {
        val tourResultModels = ToursPersistence().selectResultsByTourId(tourId)
        val results = tourResultModels.map { it.result }

        return AnalyticsUtil.averageMedian(results)
    }

    /**
     * @author SoraVWV
     *
     * (i -- i + step)  =  percent
     */
    fun handleGraph(tourId: Int, scoreStep: Int): Map<Int, Float>? {
        val tourResultModels = ToursPersistence().selectResultsByTourId(tourId)
        val result = mutableMapOf<Int, Float>()
        val maxScore: Short = ToursPersistence().selectTourById(tourId)?.maxScore ?: return null

        for (i in scoreStep until maxScore step scoreStep) {
            val count = tourResultModels.count { it.result >= i && it.result < i + scoreStep }
            result[i] = count / tourResultModels.size.toFloat();
        }

        return result
    }

}