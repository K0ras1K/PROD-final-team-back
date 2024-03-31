package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.database.schema.TeamTable
import ru.droptableusers.sampleapi.database.schema.TeamsUsersTable

class TeamsPersistence() {
    private fun resultRowToTeamModel(resultRow: ResultRow) =
        TeamModel(
            id = resultRow[TeamTable.id].value,
            name = resultRow[TeamTable.name],
            description = resultRow[TeamTable.description],
            iconUrl = resultRow[TeamTable.iconUrl],
            bannerUrl = resultRow[TeamTable.bannerUrl]
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

    fun selectAll(limit: Int? = null, offset: Long? = null): List<TeamModel> {
        return try {
            if (limit != null && offset != null){
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
                TeamsUsersTable.deleteWhere { TeamsUsersTable.userId.eq(id) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun addMember(userId: Int, teamId: Int) {
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
                    .let(::getTeammateId)
            }
        } catch (exception: Exception) {
            null
        }
    }

}