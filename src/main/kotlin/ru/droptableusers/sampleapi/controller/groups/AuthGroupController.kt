package ru.droptableusers.sampleapi.controller.groups

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.GroupAbstractController
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.models.base.GroupModel
import ru.droptableusers.sampleapi.data.models.inout.input.groups.GroupSetRequestModel
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.utils.GroupUtils

class AuthGroupController(call: ApplicationCall) : GroupAbstractController(call) {
    suspend fun setGroup() {
        runBlocking {
            if (!validateGroup(Group.ORGANIZER)) return@runBlocking
            val receive = call.receive<GroupSetRequestModel>()

            if (!GroupUtils.hasGroup(
                    GroupPersistence().select(receive.userId)!!.group,
                    GroupPersistence().select(
                        UserPersistence().selectByUsername(login)!!.id,
                    )!!.group,
                )
            ) {
                call.respond(HttpStatusCode.Unauthorized, "У вас недостаточно прав!")
                return@runBlocking
            }

            GroupPersistence().update(
                GroupModel(
                    id = receive.userId,
                    group = receive.group,
                ),
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}
