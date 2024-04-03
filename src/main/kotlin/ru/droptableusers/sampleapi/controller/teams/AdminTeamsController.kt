package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.ApplicationConstants
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.controller.GroupAbstractController
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.inout.input.teams.CreateTeamRequest
import ru.droptableusers.sampleapi.data.models.inout.input.teams.TeamTemplate
import ru.droptableusers.sampleapi.data.models.inout.input.users.AddDeleteMemberModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.teams.CreateTeamRespond
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class AdminTeamsController(call: ApplicationCall): GroupAbstractController(call) {

    suspend fun editTeamTemplate(){
        runBlocking {
            TeamsPersistence().addTeamTemplate(call.receive<TeamTemplate>())
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun createTeam() {
        runBlocking {
            // Проверка на наличие группы
            if (!validateGroup(Group.ADMIN)) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("У вас недостаточно прав!"))
                return@runBlocking
            }
            val receive = call.receive<CreateTeamRequest>()

            val targetTeamData =
                TeamModel(
                    id = 0,
                    name = receive.name,
                    description = receive.name,
                    iconUrl = ApplicationConstants.DEFAULT_ICON_URL,
                    bannerUrl = ApplicationConstants.DEFAULT_BANNER_URL,
                )

            val teamId = TeamsPersistence().insert(targetTeamData)
            TeamsPersistence().addMember(UserPersistence().selectByUsername(login)!!.id, teamId!!)

            call.respond(HttpStatusCode.OK, CreateTeamRespond(teamId))
        }
    }

    suspend fun deleteTeam(){
        runBlocking {
            if (!validateGroup(Group.ADMIN)) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("У вас недостаточно прав!"))
                return@runBlocking
            }

            val teamId = call.parameters["teamId"]!!.toInt()

            TeamsPersistence().delete(teamId)
            call.respond(HttpStatusCode.OK, CreateTeamRespond(teamId))
        }
    }

    suspend fun addMember(){
        runBlocking {
            if (!validateGroup(Group.ADMIN)) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("У вас недостаточно прав!"))
                return@runBlocking
            }

            val input = call.receive<AddDeleteMemberModel>()

            TeamsPersistence().addMember(input.userId, input.teamId)
        }
    }

    suspend fun removeMember(){
        runBlocking {
            if (!validateGroup(Group.ADMIN)) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("У вас недостаточно прав!"))
                return@runBlocking
            }

            val input = call.receive<AddDeleteMemberModel>()

            TeamsPersistence().removeMember(input.userId)
        }
    }

}