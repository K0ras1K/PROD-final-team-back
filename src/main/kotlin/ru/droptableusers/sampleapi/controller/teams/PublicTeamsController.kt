package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistance

class PublicTeamsController(val call: ApplicationCall) {
    suspend fun loadAll() {
        runBlocking {
            val limit = call.request.queryParameters["limit"]!!.toInt()
            val offset = call.request.queryParameters["offset"]!!.toLong()
            val allTeams = TeamsPersistance().selectAll(limit, offset)
            call.respond(HttpStatusCode.OK, allTeams)
        }
    }
}