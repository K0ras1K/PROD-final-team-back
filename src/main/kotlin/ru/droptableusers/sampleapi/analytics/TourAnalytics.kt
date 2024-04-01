package ru.droptableusers.sampleapi.analytics

import ru.droptableusers.sampleapi.data.models.base.TourResultModel
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.TourScoreAnalyticResponse
import ru.droptableusers.sampleapi.database.persistence.ToursPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

object TourAnalytics {
    /**
     * @return percent
     *
     * @author SoraVWV
     */
    fun handleAverageAlgebraic(
        tourId: Int,
        tagId: Int,
    ): Float {
        val tourResultModels: List<TourResultModel> =
            if (tagId >= 0) {
                ToursPersistence().selectResultsByTourId(tourId)
            } else {
                ToursPersistence().selectResultsByTourId(tourId)
                    .filter { UserPersistence().containsTagId(it.userId, tagId) }
            }

        val results = tourResultModels.map { it.result }

        return AnalyticsUtil.averageAlgebraic(results)
    }

    /**
     * @return percent
     *
     * @author SoraVWV
     */
    fun handleAverageMedian(
        tourId: Int,
        tagId: Int,
    ): Float {
        val tourResultModels: List<TourResultModel> =
            if (tagId >= 0) {
                ToursPersistence().selectResultsByTourId(tourId)
            } else {
                ToursPersistence().selectResultsByTourId(tourId)
                    .filter { UserPersistence().containsTagId(it.userId, tagId) }
            }

        val results = tourResultModels.map { it.result }

        return AnalyticsUtil.averageMedian(results)
    }

    /**
     * @author SoraVWV
     *
     * (i -- i + step)  =  percent
     */
    fun handleGraph(
        tourId: Int,
        scoreStep: Int,
        tagId: Int,
    ): Map<Int, TourScoreAnalyticResponse> {
        val tourResultModels = ToursPersistence().selectResultsByTourId(tourId)
        val result = mutableMapOf<Int, TourScoreAnalyticResponse>()
        val maxScore: Short = ToursPersistence().selectTourById(tourId)?.maxScore ?: return result

        for (i in scoreStep until maxScore step scoreStep) {
            val count =
                tourResultModels.count {
                    (if (tagId >= 0) UserPersistence().containsTagId(it.userId, tagId) else true) &&
                        it.result >= i && it.result < i + scoreStep
                }
            result[i] =
                TourScoreAnalyticResponse(
                    height = count / tourResultModels.size.toFloat(),
                    count = count,
                )
        }

        return result
    }
}
