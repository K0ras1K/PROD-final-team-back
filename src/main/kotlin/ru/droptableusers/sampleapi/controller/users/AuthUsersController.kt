package ru.droptableusers.sampleapi.controller.users

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt
import ru.droptableusers.sampleapi.ApplicationConstants
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.enums.ValidationStatus
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.EditUserModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.EditUserPassword
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.TokenRespondOutput
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel
import ru.droptableusers.sampleapi.data.models.inout.output.users.ProfileOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.users.UserInvitesRespondModel
import ru.droptableusers.sampleapi.database.persistence.*
import ru.droptableusers.sampleapi.utils.Logger
import ru.droptableusers.sampleapi.utils.Validation
import java.util.*

class AuthUsersController(call: ApplicationCall) : AbstractController(call) {
    suspend fun get() {
        val userData = UserPersistence().selectByUsername(login)!!
        val respondModel =
            ProfileOutputResponse(
                username = login,
                firstName = userData.firstName,
                lastName = userData.lastName,
                tgLogin = userData.tgLogin,
                registerAt = userData.regTime,
                group = GroupPersistence().select(userData.id)!!.group,
                id = userData.id,
                description = userData.description,
                team = TeamsPersistence().selectByUserId(userData.id) ?: -1,
                major = userData.major,
                tags = TagsPersistence().getTagsByIdList(UserPersistence().selectTagIds(userData.id))
            )
        call.respond(HttpStatusCode.OK, respondModel)
    }

    suspend fun updateUser() {
        val user = UserPersistence().selectById(call.parameters["userId"]!!.toInt())!!
        val userUpdate = call.receive<EditUserModel>()
        user.tgLogin = userUpdate.tgLogin
        user.description = userUpdate.description
        UserPersistence().update(user)
        call.respond(HttpStatusCode.OK, "{\"success\": true}")
    }

    suspend fun updateUserPassword() {
        val user = UserPersistence().selectById(call.parameters["userId"]!!.toInt())!!
        val userUpdatePassword = call.receive<EditUserPassword>()
        if (BCrypt.checkpw(userUpdatePassword.oldPassword, user.password)) {
            val validation = Validation.validatePassword(userUpdatePassword.newPassword)
            if (validation == ValidationStatus.ACCEPTED) {
                user.password = BCrypt.hashpw(userUpdatePassword.newPassword, BCrypt.gensalt())
                UserPersistence().update(user)
                val token =
                    JWT.create()
                        .withClaim("username", user.username)
                        .withClaim("passwordHash", user.password)
                        .withClaim("id", user.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 14L))
                        .sign(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN))
                call.respond(HttpStatusCode.Created, TokenRespondOutput(token))
            } else {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(validation.name))
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Старый пароль указан неверно"))
        }
    }

    // Send team request to user
    suspend fun apply() {
        runBlocking {
            val userId = call.parameters["userId"]!!.toInt()
            Logger.logger.info("UserId - $userId")
            val teamId = TeamsPersistence().selectByUserId(UserPersistence().selectByUsername(login)!!.id)!!
            Logger.logger.info("TeamId - $teamId")
            InvitePersistence().insert(
                InviteModel(
                    id = 0,
                    teamId = teamId,
                    userId = userId,
                    type = InviteStatus.TO_USER,
                ),
            )
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun accept() {
        runBlocking {
            val inviteId = call.parameters["inviteId"]!!.toInt()
            val inviteData = InvitePersistence().selectById(inviteId)!!
            InvitePersistence().delete(inviteId)
            TeamsPersistence().addMember(inviteData.userId, inviteData.teamId)
            val user = UserPersistence().selectById(inviteData.userId)
            if (user?.major != null){
                val sfm = SearchingForPersistence().selectFirstByMajor(user.major)
                if (sfm != null) SearchingForPersistence().deleteBySlotIndex(inviteData.teamId, sfm.slotIndex)
            }
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun getUserInvites() {
        runBlocking {
            val userId = UserPersistence().selectByUsername(login)!!.id
            val invites = InvitePersistence().selectModelsByUserId(userId)

            val respondModel =
                invites.map {
                    UserInvitesRespondModel(
                        TeamsPersistence().selectById(it.teamId).let { team ->
                            SmallTeamRespondModel(
                                id = team!!.id,
                                name = team.name,
                                description = team.description,
                                iconUrl = team.iconUrl,
                                bannerUrl = team.bannerUrl,
                                membersCount = TeamsPersistence().selectTeammates(team.id).size,
                            )
                        },
                        it.id,
                    )
                }
            call.respond(HttpStatusCode.OK, respondModel)
        }
    }
}
