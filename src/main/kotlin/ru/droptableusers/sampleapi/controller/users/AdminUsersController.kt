package ru.droptableusers.sampleapi.controller.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.DocumentConditionModel
import ru.droptableusers.sampleapi.data.models.base.TeamsUsersModel
import ru.droptableusers.sampleapi.data.models.inout.output.users.AdminUserOutputResponse
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

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
        return mapOf()
    }

    private fun mapTeamToUser(teamsUsers: List<TeamsUsersModel>): Map<Int, Int> {
        return teamsUsers.associate { it.userId to it.teamId }
    }

    suspend fun listUsers() {
        runBlocking {
            val users = UserPersistence().listUsers()
            val documents = DocumentsPersistence().listDocuments()
            val conditions = compareDocumentAndConditions(DocumentsPersistence().listDocumentConditions())
            val teamsUsers = mapTeamToUser(TeamsPersistence().listAllTeamsMembersRelationships())
            val teamsDb = TeamsPersistence().selectAll()
            val teams = teamsDb.associate { it.id to it }
            val result = users.map {
                AdminUserOutputResponse(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    sex = "male", // TODO Fetch from db
                    email = it.username,
                    birthdayDate = it.birthdayDate,
                    commandName = if(teamsUsers.containsKey(it.id)) teams[teamsUsers[it.id]!!]!!.name else "",
                    docsReady = true // TODO Add checks
                )
            }
            call.respond(HttpStatusCode.OK, result)
        }
    }
}