package ru.droptableusers.sampleapi.controller.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ProfileOutputResponse
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
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
            id = userData.id
        )
        call.respond(HttpStatusCode.OK, respondModel)
    }
}