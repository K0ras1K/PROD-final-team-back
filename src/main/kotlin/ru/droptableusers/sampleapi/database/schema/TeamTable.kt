package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object TeamTable : IntIdTable("teams") {
    val name = varchar("name", 32)
    val description = text("description")
    val iconUrl = varchar("iconUrl", 64)
    val bannerUrl = varchar("bannerUrl", 64)
}
