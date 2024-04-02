package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.droptableusers.sampleapi.data.enums.Major

object SearchingForTable : IntIdTable("searching_for") {
    val teamId = reference("teamId", TeamTable.id, onDelete = ReferenceOption.CASCADE)
    val slotIndex = integer("slotIndex")
    val major = enumerationByName("major", 50, Major::class)
}
