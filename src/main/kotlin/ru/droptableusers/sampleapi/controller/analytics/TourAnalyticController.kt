package ru.droptableusers.sampleapi.controller.analytics

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.analytics.TourAnalytics
import ru.droptableusers.sampleapi.analytics.UserAnalytics
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.TourAnalyticResponse
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.TourScoreAnalyticResponse
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.UserAnalyticResponse
import ru.droptableusers.sampleapi.data.models.inout.output.analytics.UserTourAnalyticResponse
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.ToursPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

// TODO add to Route (& check class)
class TourAnalyticController(call: ApplicationCall) : AbstractController(call) {

    suspend fun getTourAnalytic() {
        runBlocking {
            val tourId = call.parameters["tourId"]!!.toInt();
            val user = UserPersistence().selectByUsername(login)!!
            if (GroupPersistence().select(user.id)!!.group.ordinal > Group.MENTOR.ordinal)
                call.respond(HttpStatusCode.Forbidden)
            else
                call.respond(HttpStatusCode.OK, getTourAnalyticById(tourId))
        }
    }

    private fun getTourAnalyticById(tourId: Int): TourAnalyticResponse {
        val averageAlgebraic = TourAnalytics.handleAverageAlgebraic(tourId);
        val averageMedian = TourAnalytics.handleAverageMedian(tourId);

        val scores = TourAnalytics.handleGraph(tourId, 5);

        return TourAnalyticResponse (
            users = scores.values.toList(),
            averageAlgebraic = averageAlgebraic,
            averageMedian = averageMedian,
            name = ToursPersistence().selectTourById(tourId)!!.name
        )
    }

}