package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.Table

object ToursResultTable : Table("tours_result") {
    val name = varchar("name", 64)
    val result = float("result")
    val userId = reference("userId", UserTable.id)
}