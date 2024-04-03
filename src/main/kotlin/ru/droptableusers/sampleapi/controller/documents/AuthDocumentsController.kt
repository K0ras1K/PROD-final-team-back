package ru.droptableusers.sampleapi.controller.documents

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.FilledDocumentModel
import ru.droptableusers.sampleapi.data.models.inout.input.documents.FilledDocumentCreateInput
import ru.droptableusers.sampleapi.data.models.inout.output.documents.FilledDocumentOutputResponse
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import java.time.temporal.ChronoUnit

class AuthDocumentsController(call: ApplicationCall) : AbstractController(call) {
    suspend fun addFilledDocument() {
        runBlocking {
            val document = call.receive<FilledDocumentCreateInput>()
            var documentModel = FilledDocumentModel(
                id = 0,
                fileName = document.fileName,
                documentId = document.documentId,
                lastUpdate = System.currentTimeMillis(),
                userId = id!!
            )
            documentModel = DocumentsPersistence().insertFilledDocument(documentModel)!!
            val response = FilledDocumentOutputResponse(
                id = documentModel.id,
                fileName = documentModel.fileName,
                lastUpdate = documentModel.lastUpdate,
                documentId = documentModel.documentId,
                userId = documentModel.userId,
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }

//    suspend fun listFilledAndRequiredDocuments() {
//        runBlocking {
//            val filledDocuments = DocumentsPersistence().listFilledDocumentsByUserId(id!!)
//            val documentsDb = DocumentsPersistence().listDocuments()
//            documentsDb.forEach {doc ->
//                val documentConditions = conditions[doc.id].orEmpty()
//                documentConditions.forEach { condition ->
//                    var isRequired = false
//                    when (condition.fieldName) {
//                        "age" -> {
//                            val age = unixToLocalDateTime(it.birthdayDate).until(unixToLocalDateTime(System.currentTimeMillis()), ChronoUnit.YEARS)
//                            when (condition.condition) {
//                                "less" -> {
//                                    if (age <= condition.value.toInt()) {
//                                        isRequired = true
//                                    }
//                                }
//                                "equals" -> {
//                                    if (age == condition.value.toLong()) {
//                                        isRequired = true
//                                    }
//                                }
//                                "more" -> {
//                                    if (age >= condition.value.toInt()) {
//                                        isRequired = true
//                                    }
//                                }
//                            }
//                        }
//                        "sex" -> {
//                            val sex = "male"; // TODO Fetch from DB
//                            if (sex == condition.value) {
//                                isRequired = true
//                            }
//                        }
//                    }
//                    if (isRequired) {
//                        requiredDocsCount += 1
//                        if (filledDocuments.containsKey(it.id) && filledDocuments[it.id]!!.containsKey(doc.id)) {
//                            filledDocsCount += 1
//                        }
//                    }
//                }
//            }
//            val result =
//        }
//    }
}