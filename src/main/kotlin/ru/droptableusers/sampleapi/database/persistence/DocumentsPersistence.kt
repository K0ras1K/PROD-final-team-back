package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.DocumentConditionModel
import ru.droptableusers.sampleapi.data.models.base.DocumentModel
import ru.droptableusers.sampleapi.database.schema.DocumentConditionsTable
import ru.droptableusers.sampleapi.database.schema.DocumentsTable
import java.lang.Exception

class DocumentsPersistence {

    private fun resultRowToDocumentModel(row: ResultRow): DocumentModel =
        DocumentModel(
            id = row[DocumentsTable.id].value,
            name = row[DocumentsTable.name],
            description = row[DocumentsTable.description],
            required = row[DocumentsTable.required],
            template = row[DocumentsTable.template],
            extensions = row[DocumentsTable.extensions]
        )

    private fun resultRowToDocumentConditionModel(row: ResultRow): DocumentConditionModel =
        DocumentConditionModel(
            id = row[DocumentConditionsTable.id].value,
            condition = row[DocumentConditionsTable.condition],
            fieldName = row[DocumentConditionsTable.fieldName],
            value = row[DocumentConditionsTable.value],
            documentId = row[DocumentConditionsTable.documentId].value
        )

    fun insertDocument(documentModel: DocumentModel): DocumentModel? {
        return try {
            transaction {
                DocumentsTable.insert {
                    it[DocumentsTable.name] = documentModel.name
                    it[DocumentsTable.description] = documentModel.description
                    it[DocumentsTable.required] = documentModel.required
                    it[DocumentsTable.template] = documentModel.template
                    it[DocumentsTable.extensions] = documentModel.extensions
                }.resultedValues!!.single().let(::resultRowToDocumentModel)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    fun updateDocument(documentModel: DocumentModel): Boolean {
        return try {
            transaction {
                DocumentsTable.update({ DocumentsTable.id eq documentModel.id }) {
                    it[DocumentsTable.name] = documentModel.name
                    it[DocumentsTable.description] = documentModel.description
                    it[DocumentsTable.required] = documentModel.required
                    it[DocumentsTable.template] = documentModel.template
                    it[DocumentsTable.extensions] = documentModel.extensions
                } > 0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    fun deleteDocumentById(id: Int): Boolean {
        return try {
            transaction {
                DocumentsTable.deleteWhere { DocumentsTable.id.eq(id) } > 0
            }
        } catch (exception: Exception) {
            false
        }
    }

    fun listDocuments(): List<DocumentModel> {
        return try {
            transaction {
                DocumentsTable.selectAll()
                    .map(::resultRowToDocumentModel)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun insertDocumentCondition(documentConditionModel: DocumentConditionModel): DocumentConditionModel? {
        return try {
            transaction {
                DocumentConditionsTable.insert {
                    it[DocumentConditionsTable.documentId] = documentConditionModel.documentId
                    it[DocumentConditionsTable.fieldName] = documentConditionModel.fieldName
                    it[DocumentConditionsTable.condition] = documentConditionModel.condition
                    it[DocumentConditionsTable.value] = documentConditionModel.value
                }.resultedValues!!.single().let(::resultRowToDocumentConditionModel)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    fun updateDocumentCondition(documentConditionModel: DocumentConditionModel): Boolean {
        return try {
            transaction {
                DocumentConditionsTable.update({ DocumentConditionsTable.id eq documentConditionModel.id }) {
                    it[DocumentConditionsTable.documentId] = documentConditionModel.documentId
                    it[DocumentConditionsTable.fieldName] = documentConditionModel.fieldName
                    it[DocumentConditionsTable.condition] = documentConditionModel.condition
                    it[DocumentConditionsTable.value] = documentConditionModel.value
                } > 0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    fun deleteDocumentConditionById(id: Int): Boolean {
        return try {
            transaction {
                DocumentConditionsTable.deleteWhere { DocumentConditionsTable.id eq id } > 0
            }
        } catch (exception: Exception) {
            false
        }
    }

    fun listDocumentConditionsByDocumentId(id: Int): List<DocumentConditionModel> {
        return try {
            transaction {
                DocumentConditionsTable.selectAll()
                    .where { DocumentConditionsTable.documentId eq id }
                    .map(::resultRowToDocumentConditionModel)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

}