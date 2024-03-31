package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TeamsUsersTable: IntIdTable("teams_users") {
    val userId = reference("userId", UserTable.id).uniqueIndex()
    val teamId = reference("teamId", TeamTable.id)
}