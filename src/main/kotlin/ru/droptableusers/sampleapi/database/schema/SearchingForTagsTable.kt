package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SearchingForTagsTable : Table("searching_for_tags") {
    val tagId = reference("tagId", TagTable.id, onDelete = ReferenceOption.CASCADE)
    val searchingForId = reference("searchingForId", SearchingForTable.id, onDelete = ReferenceOption.CASCADE)
}
