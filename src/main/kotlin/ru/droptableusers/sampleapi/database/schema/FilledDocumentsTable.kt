package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object FilledDocumentsTable : IntIdTable("filled_documents") {
    val userId = reference("user_id", UserTable.id)
    val fileName = varchar("file_name", 64)
    val lastUpdate = long("last_update")
    val documentId = reference("document_id", DocumentsTable.id)
}
