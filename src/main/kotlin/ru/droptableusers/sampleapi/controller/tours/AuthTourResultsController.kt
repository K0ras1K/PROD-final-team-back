package ru.droptableusers.sampleapi.controller.tours

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.results.TourResultOutputResponse
import ru.droptableusers.sampleapi.database.persistence.ToursResultPersistence
import java.lang.Exception

class AuthTourResultsController(call: ApplicationCall) : AbstractController(call) {

    suspend fun getResultsByUserId() {
        try {
            val resultsList = ToursResultPersistence().selectByUserId(call.request.queryParameters["userId"]!!.toInt())
            val respondModels = resultsList.map { TourResultOutputResponse(
                tourName = it.name,
                result = it.result
            ) }
            call.respond(HttpStatusCode.OK, respondModels);
        } catch (exception: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(status = "Не передан корректный userId"))
        }
    }

}