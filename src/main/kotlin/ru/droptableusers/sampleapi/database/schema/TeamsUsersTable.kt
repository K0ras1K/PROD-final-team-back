package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object TeamsUsersTable : IntIdTable("teams_users") {
    val userId = reference("userId", UserTable.id).uniqueIndex()
    val teamId = reference("teamId", TeamTable.id)
}