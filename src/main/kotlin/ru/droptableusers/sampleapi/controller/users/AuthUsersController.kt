package ru.droptableusers.sampleapi.controller.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel
import ru.droptableusers.sampleapi.data.models.inout.output.users.UserInvitesRespondModel
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class AuthUsersController(call: ApplicationCall) : AbstractController(call) {
    suspend fun get() {
        val userData = UserPersistence().selectByUsername(login)!!
        val respondModel = ProfileOutputResponse(
            username = login,
            firstName = userData.firstName,
            lastName = userData.lastName,
            tgId = userData.tgLogin,
            registerAt = userData.regTime,
            group = GroupPersistence().select(userData.id)!!.group,
            id = userData.id,
            description = userData.description,
            team = TeamsPersistence().selectByUserId(userData.id) ?: -1
        )
        call.respond(HttpStatusCode.OK, respondModel)
    }

    //Send team request to user
    suspend fun apply() {
        runBlocking {
            val userId = call.parameters["userId"]!!.toInt()
            val teamId = TeamsPersistence().selectByUserId(UserPersistence().selectByUsername(login)!!.id)!!
            InvitePersistence().insert(
                InviteModel(
                    id = 0,
                    teamId = teamId,
                    userId = userId,
                    type = InviteStatus.TO_USER
                )
            )
        }
    }

    suspend fun accept() {
        runBlocking {
            val inviteId = call.parameters["inviteId"]!!.toInt()

            val inviteData = InvitePersistence().selectById(inviteId)!!
            InvitePersistence().delete(inviteId)
            TeamsPersistence().addMember(inviteData.userId, inviteData.teamId)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun getUserInvites() {
        runBlocking {
            val userId = UserPersistence().selectByUsername(login)!!.id
            val invites = InvitePersistence().selectModelsByUserId(userId)

            val respondModel = invites.map {
                UserInvitesRespondModel(
                    TeamsPersistence().selectById(it.teamId).let { team ->
                        SmallTeamRespondModel(
                            id = team!!.id,
                            name = team.name,
                            description = team.description,
                            iconUrl = team.iconUrl,
                            bannerUrl = team.bannerUrl,
                            membersCount = TeamsPersistence().selectTeammates(team.id).size
                        )
                    },
                    it.id
                )
            }
            call.respond(HttpStatusCode.OK, respondModel)
        }
    }
}