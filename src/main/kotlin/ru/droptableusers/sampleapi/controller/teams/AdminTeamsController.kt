package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.input.teams.TeamTemplate
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence

class AdminTeamsController(call: ApplicationCall): AbstractController(call) {

    suspend fun editTeamTemplate(){
        runBlocking {
            TeamsPersistence().addTeamTemplate(call.receive<TeamTemplate>())
            call.respond(HttpStatusCode.OK)
        }
    }
}