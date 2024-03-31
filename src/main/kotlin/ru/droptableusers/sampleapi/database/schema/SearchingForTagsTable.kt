package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.Table

object SearchingForTagsTable: Table("searching_for_tags") {
    val tagId = reference("tagId", TagTable.id)
    val searchingForId = reference("searchingForId", SearchingForTable.id)
}