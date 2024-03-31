package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.Table
import ru.droptableusers.sampleapi.data.enums.Group

object GroupTable : Table("groups") {
    val id = reference("userId", UserTable.id)
    val group = enumerationByName("group", 50, Group::class)
}