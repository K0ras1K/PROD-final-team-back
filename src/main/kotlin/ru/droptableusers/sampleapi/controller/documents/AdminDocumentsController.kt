package ru.droptableusers.sampleapi.controller.documents

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.DocumentConditionModel
import ru.droptableusers.sampleapi.data.models.base.DocumentModel
import ru.droptableusers.sampleapi.data.models.inout.input.documents.DocumentCreateInput
import ru.droptableusers.sampleapi.data.models.inout.input.documents.DocumentUpdateInput
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.documents.DocumentConditionOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.documents.DocumentOutputResponse
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence

class AdminDocumentsController(call: ApplicationCall) : AbstractController(call) {
    private fun compareDocumentAndConditions(conditions: List<DocumentConditionModel>): Map<Int, List<DocumentConditionModel>> {
        val result = HashMap<Int, MutableList<DocumentConditionModel>>()
        conditions.forEach {
            if (result.containsKey(it.documentId) && result[it.documentId] != null) {
                result[it.documentId]!!.add(it)
            } else {
                result[it.documentId] = mutableListOf(it)
            }
        }
        return result
    }

    suspend fun listDocuments() {
        runBlocking {
            // Only for admin and org
            if (userGroup.group.ordinal > 1) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("Forbidden"))
                return@runBlocking
            }
            val documents = DocumentsPersistence().listDocuments()
            val conditions = compareDocumentAndConditions(DocumentsPersistence().listDocumentConditions())
            // TODO Возвращать полную ссылку на файл?
            val respondModels =
                documents.map {
                    DocumentOutputResponse(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        required = it.required,
                        template = it.template,
                        extensions = it.extensions.split(","),
                        conditions =
                            conditions[it.id].orEmpty().map { condition ->
                                DocumentConditionOutputResponse(
                                    id = condition.id,
                                    fieldName = condition.fieldName,
                                    condition = condition.condition,
                                    value = condition.value,
                                )
                            },
                    )
                }
            call.respond(HttpStatusCode.OK, respondModels)
        }
    }

    suspend fun addDocument() {
        runBlocking {
            if (userGroup.group.ordinal > 1) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("Forbidden"))
                return@runBlocking
            }
            val inputDocument = call.receive<DocumentCreateInput>()
            val documentModel =
                DocumentModel(
                    id = 0,
                    name = inputDocument.name,
                    description = inputDocument.description,
                    required = inputDocument.required,
                    template = inputDocument.template,
                    extensions = inputDocument.extensions.joinToString(","),
                )
            val document = DocumentsPersistence().insertDocument(documentModel)!!
            val conditions = mutableSetOf<DocumentConditionModel>()
            inputDocument.conditions.forEach {
                val conditionModel =
                    DocumentConditionModel(
                        id = 0,
                        documentId = document.id,
                        fieldName = it.fieldName,
                        condition = it.condition,
                        value = it.value,
                    )
                conditions.add(DocumentsPersistence().insertDocumentCondition(conditionModel)!!)
            }
            // TODO add Telegram notification
            val response =
                DocumentOutputResponse(
                    id = document.id,
                    name = document.name,
                    description = document.description,
                    required = document.required,
                    template = document.template,
                    extensions = inputDocument.extensions,
                    conditions =
                        conditions.map {
                            DocumentConditionOutputResponse(
                                id = it.id,
                                fieldName = it.fieldName,
                                condition = it.condition,
                                value = it.value,
                            )
                        },
                )
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun editDocument() {
        runBlocking {
            if (userGroup.group.ordinal > 1) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("Forbidden"))
                return@runBlocking
            }
            val inputDocument = call.receive<DocumentUpdateInput>()
            val documentModel =
                DocumentModel(
                    id = inputDocument.id,
                    name = inputDocument.name,
                    description = inputDocument.description,
                    required = inputDocument.required,
                    template = inputDocument.template,
                    extensions = inputDocument.extensions.joinToString(","),
                )
            DocumentsPersistence().updateDocument(documentModel)
            inputDocument.conditions.forEach {
                val conditionModel =
                    DocumentConditionModel(
                        id = it.id,
                        documentId = inputDocument.id,
                        fieldName = it.fieldName,
                        condition = it.condition,
                        value = it.value,
                    )
                DocumentsPersistence().updateDocumentCondition(conditionModel)
            }
            call.respond(HttpStatusCode.OK, "{\"success\": true}")
        }
    }

    suspend fun deleteDocument() {
        runBlocking {
            val documentId = call.parameters["documentId"]!!.toInt()
            DocumentsPersistence().deleteDocumentById(documentId)
            call.respond(HttpStatusCode.OK, "{\"success\": true}")
        }
    }
}
