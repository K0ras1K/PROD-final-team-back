package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object TeamsUsersTable: IdTable<String>("teams_users") {
    override val id: Column<EntityID<String>> = text("recordId").entityId()
    val userId = reference("userId", UserTable.id)
    val teamId = reference("teamId", TeamTable.id)
}