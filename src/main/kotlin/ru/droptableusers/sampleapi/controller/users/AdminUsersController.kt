package ru.droptableusers.sampleapi.controller.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.DocumentConditionModel
import ru.droptableusers.sampleapi.data.models.base.FilledDocumentModel
import ru.droptableusers.sampleapi.data.models.base.TeamsUsersModel
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.NotifyListModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.users.AdminUserOutputResponse
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.utils.NotifyUtils
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.HashMap

class AdminUsersController(call: ApplicationCall) : AbstractController(call) {
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

    private fun compareUserAndFilledDocuments(filledDocuments: List<FilledDocumentModel>): Map<Int, Map<Int, FilledDocumentModel>> {
        val result = HashMap<Int, MutableMap<Int, FilledDocumentModel>>()
        filledDocuments.forEach {
            if (result.containsKey(it.userId) && result[it.userId] != null) {
                result[it.userId]!![it.documentId] = it
            } else {
                result[it.userId] = mutableMapOf(Pair(it.documentId, it))
            }
        }
        return result
    }

    private fun mapTeamToUser(teamsUsers: List<TeamsUsersModel>): Map<Int, Int> {
        return teamsUsers.associate { it.userId to it.teamId }
    }

    private fun unixToLocalDateTime(time: Long): LocalDate {
        return Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    suspend fun listUsers() {
        runBlocking {
            if (userGroup.group.ordinal > 4) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("Forbidden"))
                return@runBlocking
            }
            val users = UserPersistence().listUsers()
            val documents = DocumentsPersistence().listDocuments()
            val conditions = compareDocumentAndConditions(DocumentsPersistence().listDocumentConditions())
            val filledDocuments = compareUserAndFilledDocuments(DocumentsPersistence().listFilledDocuments())
            val teamsUsers = mapTeamToUser(TeamsPersistence().listAllTeamsMembersRelationships())
            val teamsDb = TeamsPersistence().selectAll()
            val teams = teamsDb.associate { it.id to it }
            val result = users.map {
                var filledDocsCount = 0
                var requiredDocsCount = 0
                documents.forEach {doc ->
                    if (doc.required) {
                        val documentConditions = conditions[doc.id].orEmpty()
                        documentConditions.forEach { condition ->
                            var isRequired = false
                            when (condition.fieldName) {
                                "age" -> {
                                    val age = unixToLocalDateTime(it.birthdayDate).until(unixToLocalDateTime(System.currentTimeMillis()), ChronoUnit.YEARS)
                                    when (condition.condition) {
                                        "less" -> {
                                            if (age <= condition.value.toInt()) {
                                                isRequired = true
                                            }
                                        }
                                        "equals" -> {
                                            if (age == condition.value.toLong()) {
                                                isRequired = true
                                            }
                                        }
                                        "more" -> {
                                            if (age >= condition.value.toInt()) {
                                                isRequired = true
                                            }
                                        }
                                    }
                                }
                                "sex" -> {
                                    val sex = "male"; // TODO Fetch from DB
                                    if (sex == condition.value) {
                                        isRequired = true
                                    }
                                }
                            }
                            if (isRequired) {
                                requiredDocsCount += 1
                                if (filledDocuments.containsKey(it.id) && filledDocuments[it.id]!!.containsKey(doc.id)) {
                                    filledDocsCount += 1
                                }
                            }
                        }
                    }
                }
                AdminUserOutputResponse(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    sex = "male", // TODO Fetch from db
                    email = it.username,
                    birthdayDate = it.birthdayDate,
                    commandName = if(teamsUsers.containsKey(it.id)) teams[teamsUsers[it.id]!!]!!.name else "",
                    filledDocs = filledDocsCount,
                    requiredDocs = requiredDocsCount
                )
            }
            call.respond(HttpStatusCode.OK, result)
        }
    }

    suspend fun listNotFilledUsers() {
        runBlocking {
            val documentId = call.request.queryParameters["documentId"]!!.toInt()
            val document = DocumentsPersistence().selectDocumentById(documentId)!!
            val documentConditions = DocumentsPersistence().listDocumentConditionsByDocumentId(documentId)
            val filledDocuments = (DocumentsPersistence().listFilledDocumentsByDocumentId(documentId)).associate { it.userId to it }
            val users = UserPersistence().listUsers()
            val result = mutableListOf<UserModel>()
            users.forEach {
                if (document.required) {
                    documentConditions.forEach { condition ->
                        var isRequired = false
                        when (condition.fieldName) {
                            "age" -> {
                                val age = unixToLocalDateTime(it.birthdayDate).until(unixToLocalDateTime(System.currentTimeMillis()), ChronoUnit.YEARS)
                                when (condition.condition) {
                                    "less" -> {
                                        if (age <= condition.value.toInt()) {
                                            isRequired = true
                                        }
                                    }
                                    "equals" -> {
                                        if (age == condition.value.toLong()) {
                                            isRequired = true
                                        }
                                    }
                                    "more" -> {
                                        if (age >= condition.value.toInt()) {
                                            isRequired = true
                                        }
                                    }
                                }
                            }
                            "sex" -> {
                                val sex = "male"; // TODO Fetch from DB
                                if (sex == condition.value) {
                                    isRequired = true
                                }
                            }
                        }
                        if (isRequired) {
                            if (!filledDocuments.containsKey(it.id)) {
                                result.add(it)
                            }
                        }
                    }
                }
            }
            call.respond(HttpStatusCode.OK, result)
        }
    }

    suspend fun telegramNotify() {
        runBlocking {
            val notifyList = call.receive<NotifyListModel>()
            NotifyUtils.notifyByIds(notifyList.userIds, notifyList.message)
            call.respond(HttpStatusCode.OK, "{\"status\": \"OK\"}")
        }
    }
}
