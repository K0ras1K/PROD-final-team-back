package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object DocumentConditionsTable : IntIdTable("document_conditions") {
    val documentId = reference("document_id", DocumentsTable.id, onDelete = ReferenceOption.CASCADE)
    val fieldName = varchar("field_name", 64)
    val condition = varchar("condition", 16)
    val value = varchar("value", 32)
}
