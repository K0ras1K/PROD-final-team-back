package ru.droptableusers.sampleapi.controller.tours

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.results.TourOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.results.TourResultOutputResponse
import ru.droptableusers.sampleapi.database.persistence.ToursPersistence

class AuthTourResultsController(call: ApplicationCall) : AbstractController(call) {
    suspend fun getResultsByUserId() {
        try {
            val resultsList = ToursPersistence().selectResultsByUserId(call.request.queryParameters["userId"]!!.toInt())
            val toursIds = resultsList.map { it.tourId }.toSet()
            val tours = toursIds.associateWith { ToursPersistence().selectTourById(it) }
            val respondModels =
                resultsList.map {
                    TourResultOutputResponse(
                        tour =
                            TourOutputResponse(
                                name = tours[it.tourId]!!.name,
                                year = tours[it.tourId]!!.year,
                                maxScore = tours[it.tourId]!!.maxScore,
                            ),
                        result = it.result,
                        userId = it.userId,
                    )
                }
            call.respond(HttpStatusCode.OK, respondModels)
        } catch (exception: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(status = "Не передан корректный userId"))
        }
    }

    suspend fun getResultsByTourName() {
        try {
            val tourId = call.request.queryParameters["tourId"]!!.toInt()
            val resultsList = ToursPersistence().selectResultsByTourId(tourId)
            val tour = ToursPersistence().selectTourById(tourId)!!
            val tourResponse =
                TourOutputResponse(
                    name = tour.name,
                    year = tour.year,
                    maxScore = tour.maxScore,
                )
            val respondModels =
                resultsList.map {
                    TourResultOutputResponse(
                        result = it.result,
                        userId = it.userId,
                        tour = tourResponse,
                    )
                }
            call.respond(HttpStatusCode.OK, respondModels)
        } catch (exception: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(status = "Не передан корректный tourId"))
        }
    }

    suspend fun listTours() {
        val resultsList = ToursPersistence().listAllTours()
        val respondModels =
            resultsList.map {
                TourOutputResponse(
                    name = it.name,
                    year = it.year,
                    maxScore = it.maxScore,
                )
            }
        call.respond(HttpStatusCode.OK, respondModels)
    }
}
