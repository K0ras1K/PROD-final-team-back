package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import ru.droptableusers.sampleapi.data.enums.InviteStatus

object InviteTable: IntIdTable("invites"){
    val userId = reference("userId", UserTable.id)
    val teamId = reference("teamId", TeamTable.id)
    val type = enumerationByName("type", 16, InviteStatus::class)
}