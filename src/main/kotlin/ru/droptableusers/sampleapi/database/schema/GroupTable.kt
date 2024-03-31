package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.Table
import ru.droptableusers.sampleapi.data.enums.Group

object GroupTable : Table("groups") {
    val username = varchar("username", 50)
    val group = enumerationByName("group", 50, Group::class)
}