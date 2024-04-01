package ru.droptableusers.sampleapi.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.database.schema.GroupTable.group
import ru.droptableusers.sampleapi.utils.GroupUtils

abstract class GroupAbstractController(call: ApplicationCall) : AbstractController(call) {
    suspend fun validateGroup(group: Group): Boolean
        = GroupUtils.hasGroup(login, group)

}
