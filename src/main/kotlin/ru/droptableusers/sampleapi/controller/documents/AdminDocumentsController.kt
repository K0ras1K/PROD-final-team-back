package ru.droptableusers.sampleapi.controller.documents

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.DocumentConditionModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.documents.DocumentConditionOutputResponse
import ru.droptableusers.sampleapi.data.models.inout.output.documents.DocumentOutputResponse
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence

class AdminDocumentsController(call: ApplicationCall) : AbstractController(call) {

    private fun compareDocumentAndConditions(conditions: List<DocumentConditionModel>): Map<Int, List<DocumentConditionModel>>  {
        val result = HashMap<Int, ArrayList<DocumentConditionModel>>()
        conditions.forEach {
            if (result.containsKey(it.documentId) && result[it.documentId] != null) {
                result[it.documentId]!!.add(it)
            } else {
                result[it.documentId] = arrayListOf(it)
            }
        };
        return mapOf()
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
            val respondModels = documents.map {
                DocumentOutputResponse(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    required = it.required,
                    template = it.template,
                    extensions = it.extensions.split(","),
                    conditions = conditions[it.id].orEmpty().map { condition ->
                        DocumentConditionOutputResponse(
                            id = condition.id,
                            fieldName = condition.fieldName,
                            condition = condition.condition,
                            value = condition.value
                        )
                    }
                )
            }
            call.respond(HttpStatusCode.OK, respondModels)
        }
    }

}