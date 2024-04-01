package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object TagTable : IntIdTable("tags") {
    val text = varchar("text", 16).uniqueIndex()
}
