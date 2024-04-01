package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object DocumentsTable : IntIdTable("documents") {
    var name = varchar("name", 256)
    var description = text("description")
    var required = bool("required")
    var template = varchar("template", 64)
    var extensions = varchar("extension", 512)
}
