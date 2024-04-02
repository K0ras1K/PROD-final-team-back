package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object TeamTemplateTable : IntIdTable("team_template") {
    val jsonTemplate = varchar("jsonTemplate", 2048)
}
