package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.input.teams.TeamTemplate
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence

class AdminTeamsController(call: ApplicationCall) : AbstractController(call) {
    suspend fun getTeamTemplate() {
        val template = TeamsPersistence().selectTeamTemplate()
        if (template != null) {
            call.respond(HttpStatusCode.OK, template)
        } else {
            call.respond(HttpStatusCode.NoContent, ErrorResponse("Темплейт отсутствует"))
        }
    }

    suspend fun editTemplate() {
        TeamsPersistence().addTeamTemplate(call.receive<TeamTemplate>())
        call.respond(HttpStatusCode.OK)
    }
}
