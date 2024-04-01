package ru.droptableusers.sampleapi.analytics

import ru.droptableusers.sampleapi.data.models.inout.output.analytics.TourScoreAnalyticResponse
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
    fun handleGraph(tourId: Int, scoreStep: Int): Map<Int, TourScoreAnalyticResponse> {
        val tourResultModels = ToursPersistence().selectResultsByTourId(tourId)
        val result = mutableMapOf<Int, TourScoreAnalyticResponse>()
        val maxScore: Short = ToursPersistence().selectTourById(tourId)?.maxScore ?: return result

        for (i in scoreStep until maxScore step scoreStep) {
            val count = tourResultModels.count { it.result >= i && it.result < i + scoreStep }
            result[i] = TourScoreAnalyticResponse(
                height = count / tourResultModels.size.toFloat(),
                count = count
            );
        }

        return result
    }

}