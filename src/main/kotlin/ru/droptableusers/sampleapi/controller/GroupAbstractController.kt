package ru.droptableusers.sampleapi.controller

import io.ktor.server.application.*
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.utils.GroupUtils

abstract class GroupAbstractController(call: ApplicationCall) : AbstractController(call) {
    suspend fun validateGroup(group: Group): Boolean = GroupUtils.hasGroup(login, group)
}
