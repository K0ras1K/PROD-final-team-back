package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object DocumentConditionsTable : IntIdTable("document_conditions") {
    val documentId = reference("document_id", DocumentsTable.id)
    val fieldName = varchar("field_name", 64)
    val condition = varchar("condition", 16)
    val value = varchar("value", 32)
}
