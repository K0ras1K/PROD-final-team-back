package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.ApplicationConstants
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.inout.input.teams.CreateTeamRequest
import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel
import ru.droptableusers.sampleapi.data.models.inout.output.teams.TeamRespondModel
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class AuthTeamsController(call: ApplicationCall) : AbstractController(call) {
    suspend fun loadTeam() {
        runBlocking {
            val teamId = call.parameters["id"]!!.toInt()
            val teamData = TeamsPersistence().selectById(teamId)!!.let {
                SmallTeamRespondModel(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    iconUrl = it.iconUrl,
                    bannerUrl = it.bannerUrl,
                    membersCount = TeamsPersistence().selectTeammates(it.id).size
                )
            }
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

    suspend fun createTeam() {
        runBlocking {
            val receive = call.receive<CreateTeamRequest>()

            val targetTeamData = TeamModel(
                id = 0,
                name = receive.name,
                description = receive.name,
                iconUrl = ApplicationConstants.DEFAULT_ICON_URL,
                bannerUrl = ApplicationConstants.DEFAULT_BANNER_URL
            )

            val teamId = TeamsPersistence().insert(targetTeamData)
            TeamsPersistence().addMember(UserPersistence().selectByUsername(login)!!.id, teamId!!)
            call.respond(HttpStatusCode.OK, teamId)
        }
    }
}