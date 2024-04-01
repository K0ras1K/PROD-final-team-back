package ru.droptableusers.sampleapi.controller.analytics

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.analytics.TourAnalytics
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.TourAnalyticResponse
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.ToursPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class TourAnalyticController(call: ApplicationCall) : AbstractController(call) {
    suspend fun getTourAnalytic() {
        runBlocking {
            val tourId = call.parameters["tourId"]!!.toInt()
            val user = UserPersistence().selectByUsername(login)!!
            if (GroupPersistence().select(user.id)!!.group.ordinal > Group.MENTOR.ordinal) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                val tagId = call.parameters["tagId"]?.toInt() ?: -1
                val step = call.parameters["step"]?.toInt() ?: 5
                call.respond(HttpStatusCode.OK, getTourAnalyticByIdAndTagId(tourId, step, tagId))
            }
        }
    }

    private fun getTourAnalyticByIdAndTagId(
        tourId: Int,
        step: Int,
        tagId: Int,
    ): TourAnalyticResponse {
        val averageAlgebraic = TourAnalytics.handleAverageAlgebraic(tourId, tagId)
        val averageMedian = TourAnalytics.handleAverageMedian(tourId, tagId)

        val scores = TourAnalytics.handleGraph(tourId, step, tagId)

        return TourAnalyticResponse(
            users = scores.values.toList(),
            averageAlgebraic = averageAlgebraic,
            averageMedian = averageMedian,
            name = ToursPersistence().selectTourById(tourId)!!.name,
        )
    }
}
