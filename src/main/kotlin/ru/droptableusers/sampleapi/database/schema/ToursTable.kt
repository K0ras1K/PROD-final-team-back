package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object ToursTable : IntIdTable("tours") {
    val name = varchar("name", 64)
    val year = short("year")
    val maxScore = short("max_score")
}
