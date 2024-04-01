package ru.droptableusers.sampleapi.analytics

object AnalyticsUtil {
    fun averageMedian(results: List<Float>): Float {
        val average = results.average().toFloat()

        val sortedResults = results.sorted()
        val size = sortedResults.size
        val median =
            if (size % 2 == 0) {
                (sortedResults[size / 2 - 1] + sortedResults[size / 2]) / 2.0f
            } else {
                sortedResults[size / 2]
            }

        return if (average < median) average else median
    }

    fun averageAlgebraic(results: List<Float>): Float {
        var sum = 0f
        var count = 0

        for (value in results) {
            sum += value
            count++
        }

        return sum / count
    }
}
