package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SearchingForTable : IntIdTable("searching_for") {
    val teamId = reference("teamId", TeamTable.id, onDelete = ReferenceOption.CASCADE)
}