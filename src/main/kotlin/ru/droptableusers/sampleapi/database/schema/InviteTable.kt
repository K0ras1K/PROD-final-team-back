package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.droptableusers.sampleapi.data.enums.InviteStatus

object InviteTable : IntIdTable("invites") {
    val userId = reference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val teamId = reference("teamId", TeamTable.id, onDelete = ReferenceOption.CASCADE)
    val type = enumerationByName("type", 16, InviteStatus::class)
}
