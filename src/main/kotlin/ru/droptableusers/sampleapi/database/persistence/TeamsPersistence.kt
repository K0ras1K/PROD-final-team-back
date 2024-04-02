package ru.droptableusers.sampleapi.database.persistence

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.base.TeamsUsersModel
import ru.droptableusers.sampleapi.data.models.inout.input.teams.TeamTemplate
import ru.droptableusers.sampleapi.database.schema.TeamTable
import ru.droptableusers.sampleapi.database.schema.TeamTemplateTable
import ru.droptableusers.sampleapi.database.schema.TeamsUsersTable

class TeamsPersistence() {
    private fun resultRowToTeamModel(resultRow: ResultRow) =
        TeamModel(
            id = resultRow[TeamTable.id].value,
            name = resultRow[TeamTable.name],
            description = resultRow[TeamTable.description],
            iconUrl = resultRow[TeamTable.iconUrl],
            bannerUrl = resultRow[TeamTable.bannerUrl],
        )

    fun insert(teamModel: TeamModel): Int? {
        return try {
            transaction {
                TeamTable.insert {
                    it[TeamTable.name] = teamModel.name
                    it[TeamTable.description] = teamModel.description
                    it[TeamTable.iconUrl] = teamModel.iconUrl
                    it[TeamTable.bannerUrl] = teamModel.bannerUrl
                }.resultedValues!!.single().let { it[TeamTable.id].value }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun selectById(id: Int): TeamModel? {
        return try {
            transaction {
                TeamTable.selectAll()
                    .where { TeamTable.id.eq(id) }
                    .single()
                    .let(::resultRowToTeamModel)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun selectAll(
        limit: Int? = null,
        offset: Long? = null,
    ): List<TeamModel> {
        return try {
            if (limit != null && offset != null) {
                transaction {
                    TeamTable.selectAll()
                        .limit(limit, offset = offset)
                        .map(::resultRowToTeamModel)
                }
            } else {
                transaction {
                    TeamTable.selectAll()
                        .map(::resultRowToTeamModel)
                }
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun update(teamModel: TeamModel): Boolean {
        return try {
            transaction {
                TeamTable.update({ TeamTable.id.eq(teamModel.id) }) {
                    it[TeamTable.name] = teamModel.name
                    it[TeamTable.description] = teamModel.description
                    it[TeamTable.iconUrl] = teamModel.iconUrl
                    it[TeamTable.bannerUrl] = teamModel.bannerUrl
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun delete(id: Int): Boolean {
        return try {
            transaction {
                TeamTable.deleteWhere { TeamTable.id.eq(id) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun removeMember(userId: Int): Boolean {
        return try {
            transaction {
                TeamsUsersTable.deleteWhere { TeamsUsersTable.userId.eq(userId) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun addMember(
        userId: Int,
        teamId: Int,
    ) {
        try {
            transaction {
                TeamsUsersTable.insert {
                    it[TeamsUsersTable.userId] = userId
                    it[TeamsUsersTable.teamId] = teamId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getTeammateId(resultRow: ResultRow): Int = resultRow[TeamsUsersTable.userId].value

    private fun getTeamId(resultRow: ResultRow): Int = resultRow[TeamsUsersTable.teamId].value

    fun selectTeammates(id: Int): List<Int> {
        return try {
            transaction {
                TeamsUsersTable.selectAll()
                    .where { TeamsUsersTable.teamId.eq(id) }
                    .map(::getTeammateId)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectByUserId(id: Int): Int? {
        return try {
            transaction {
                TeamsUsersTable.selectAll()
                    .where { TeamsUsersTable.userId.eq(id) }
                    .single()
                    .let(::getTeamId)
            }
        } catch (exception: Exception) {
            null
        }
    }

    private fun resultRowToTeamsUsersModel(row: ResultRow): TeamsUsersModel =
        TeamsUsersModel(
            teamId = row[TeamsUsersTable.teamId].value,
            userId = row[TeamsUsersTable.userId].value,
        )

    fun listAllTeamsMembersRelationships(): List<TeamsUsersModel> {
        return try {
            transaction {
                TeamsUsersTable.selectAll()
                    .map(::resultRowToTeamsUsersModel)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun selectAllMembers(): List<Int> {
        return try {
            transaction {
                TeamsUsersTable.selectAll()
                    .map(::getTeammateId)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectTeamTemplate(): TeamTemplate? {
        return try {
            transaction {
                TeamTemplateTable.selectAll()
                    .where { TeamTemplateTable.id.eq(1) }
                    .single()
                    .let {
                        Json.decodeFromString<TeamTemplate>(it[TeamTemplateTable.jsonTemplate])
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun templateExists(): Boolean {
        return try {
            transaction {
                TeamTemplateTable.selectAll()
                    .where { TeamTemplateTable.id.eq(1) }
                    .map { it }.isNotEmpty()
            }
        } catch (e: Exception) {
            false
        }
    }

    fun addTeamTemplate(template: TeamTemplate) {
        try {
            transaction {
                if (templateExists()) {
                    TeamTemplateTable.update({ TeamTemplateTable.id.eq(1) }) {
                        it[TeamTemplateTable.jsonTemplate] = Json.encodeToString(template)
                    }
                } else {
                    TeamTemplateTable.insert {
                        it[TeamTemplateTable.jsonTemplate] = Json.encodeToString(template)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
