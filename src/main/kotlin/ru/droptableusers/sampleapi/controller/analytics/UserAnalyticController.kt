package ru.droptableusers.sampleapi.controller.analytics

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.analytics.UserAnalytics
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.UserAnalyticResponse
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.UserTourAnalyticResponse
import ru.droptableusers.sampleapi.database.persistence.ToursPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

// TODO add to Route (& check class)
class UserAnalyticController(call: ApplicationCall) : AbstractController(call) {
    suspend fun getUserAnalytic() {
        runBlocking {
            val userId = call.parameters["id"]?.toInt()
            if (userId == null) {
                val id = UserPersistence().selectByUsername(login)!!.id
                call.respond(HttpStatusCode.OK, getUserAnalyticById(id))
            } else {
                call.respond(HttpStatusCode.OK, getUserAnalyticById(userId))
            }
        }
    }

    private fun getUserAnalyticById(userId: Int): UserAnalyticResponse {
        val averageAlgebraic = UserAnalytics.handleAverageAlgebraic(userId)
        val averageMedian = UserAnalytics.handleAverageMedian(userId)

        val tours = mutableListOf<UserTourAnalyticResponse>()
        for (tourResult in ToursPersistence().selectResultsByTourId(userId)) {
            val tourModel = ToursPersistence().selectTourById(tourResult.tourId)!!
            tours.add(
                UserTourAnalyticResponse(
                    name = tourModel.name,
                    maxScore = tourModel.maxScore,
                    score = tourResult.result,
                ),
            )
        }

        return UserAnalyticResponse(
            tours = tours,
            averageAlgebraic = averageAlgebraic,
            averageMedian = averageMedian,
        )
    }
}
