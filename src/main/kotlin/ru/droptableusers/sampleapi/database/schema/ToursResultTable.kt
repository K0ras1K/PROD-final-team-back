package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ToursResultTable : Table("tours_result") {
    val tourId = reference("tourId", ToursTable.id, onDelete = ReferenceOption.CASCADE)
    val result = float("result")
    val userId = reference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE)
}
