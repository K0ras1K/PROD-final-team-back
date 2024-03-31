package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.teams.TeamRespondModel
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class AuthTeamsController(call: ApplicationCall) : AbstractController(call) {
    suspend fun loadTeam() {
        runBlocking {
            val teamId = call.parameters["id"]!!.toInt()
            val teamData = TeamsPersistence().selectById(teamId)
            val users: List<ProfileOutputResponse> = UserPersistence().selectByIdList(
                TeamsPersistence().selectTeammates(
                    teamId
                )
            ).map {
                ProfileOutputResponse(
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    tgId = it.tgLogin,
                    registerAt = it.regTime
                )
            }
            val teamOutput = TeamRespondModel(
                team = teamData!!,
                users = users
            )
            call.respond(HttpStatusCode.OK, teamOutput)
        }
    }
}