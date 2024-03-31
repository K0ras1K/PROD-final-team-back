package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object SearchingForTable : IntIdTable("searching_for") {
    val teamId = reference("teamId", TeamTable.id)
}