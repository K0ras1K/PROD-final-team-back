package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object TeamsUsersTable : IntIdTable("teams_users") {
    val userId = reference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val teamId = reference("teamId", TeamTable.id, onDelete = ReferenceOption.CASCADE)
}
