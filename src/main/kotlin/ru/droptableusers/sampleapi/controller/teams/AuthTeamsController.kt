package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.ApplicationConstants
import ru.droptableusers.sampleapi.controller.GroupAbstractController
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.inout.input.teams.CreateTeamRequest
import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.teams.InvitesRespondModel
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel
import ru.droptableusers.sampleapi.data.models.inout.output.teams.TeamRespondModel
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class AuthTeamsController(call: ApplicationCall) : GroupAbstractController(call) {
    suspend fun loadTeam() {
        runBlocking {
            val teamId = call.parameters["id"]!!.toInt()
            val teamData =
                TeamsPersistence().selectById(teamId)!!.let {
                    SmallTeamRespondModel(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        iconUrl = it.iconUrl,
                        bannerUrl = it.bannerUrl,
                        membersCount = TeamsPersistence().selectTeammates(it.id).size,
                    )
                }
            val users: List<ProfileOutputResponse> =
                UserPersistence().selectByIdList(
                    TeamsPersistence().selectTeammates(
                        teamId,
                    ),
                ).map {
                    ProfileOutputResponse(
                        username = it.username,
                        firstName = it.firstName,
                        lastName = it.lastName,
                        tgId = it.tgLogin,
                        registerAt = it.regTime,
                        group = GroupPersistence().select(it.id)!!.group,
                        id = it.id,
                        description = it.description,
                        team = TeamsPersistence().selectByUserId(it.id) ?: -1,
                    )
                }
            val teamOutput =
                TeamRespondModel(
                    team = teamData,
                    users = users,
                )
            call.respond(HttpStatusCode.OK, teamOutput)
        }
    }

    suspend fun createTeam() {
        runBlocking {
            // Проверка на наличие группы
            validateGroup(Group.MEMBER)
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
            call.respond(HttpStatusCode.OK, teamId)
        }
    }

    // Request from user to team
    suspend fun apply() {
        runBlocking {
            val teamId = call.parameters["teamId"]!!.toInt()
            val userId = UserPersistence().selectByUsername(login)!!.id

            InvitePersistence().insert(
                InviteModel(
                    id = 0,
                    teamId = teamId,
                    userId = userId,
                    type = InviteStatus.TO_TEAM,
                ),
            )
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun accept() {
        runBlocking {
            val inviteId = call.parameters["inviteId"]!!.toInt()
            val inviteData = InvitePersistence().selectById(inviteId)!!
            val teamId = inviteData.teamId
            val userId = inviteData.userId

            TeamsPersistence().addMember(userId, teamId)
            InvitePersistence().delete(inviteId)

            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun loadRequests() {
        runBlocking {
            val teamId = TeamsPersistence().selectByUserId(UserPersistence().selectByUsername(login)!!.id)!!
            val requests = InvitePersistence().selectByTeamId(teamId, InviteStatus.TO_TEAM)
            val respondModel =
                requests.map {
                    InvitesRespondModel(
                        user =
                            UserPersistence().selectById(it.userId)
                                .let { user ->
                                    ProfileOutputResponse(
                                        username = user!!.username,
                                        firstName = user.firstName,
                                        lastName = user.lastName,
                                        tgId = user.tgLogin,
                                        registerAt = user.regTime,
                                        group = GroupPersistence().select(user.id)!!.group,
                                        id = user.id,
                                        description = user.description,
                                        team = TeamsPersistence().selectByUserId(user.id) ?: -1,
                                    )
                                },
                        id = it.id,
                    )
                }
            call.respond(HttpStatusCode.OK, respondModel)
        }
    }
}
