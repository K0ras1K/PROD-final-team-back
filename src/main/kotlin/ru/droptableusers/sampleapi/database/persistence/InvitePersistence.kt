package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.database.schema.InviteTable

class InvitePersistence {
    private fun resultRowToInvite(resultRow: ResultRow) =
        InviteModel(
            teamId = resultRow[InviteTable.teamId].value,
            userId = resultRow[InviteTable.userId].value,
            type = resultRow[InviteTable.type],
            id = resultRow[InviteTable.id].value,
        )

    fun insert(inviteModel: InviteModel) {
        try {
            transaction {
                InviteTable.insert {
                    it[InviteTable.teamId] = inviteModel.teamId
                    it[InviteTable.userId] = inviteModel.userId
                    it[InviteTable.type] = inviteModel.type
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectByTeamId(
        teamId: Int,
        status: InviteStatus,
    ): List<InviteModel> {
        return try {
            transaction {
                InviteTable.selectAll()
                    .where { InviteTable.teamId.eq(teamId) and InviteTable.type.eq(status) }
                    .map(::resultRowToInvite)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectModelsByUserId(userId: Int): List<InviteModel> {
        return try {
            transaction {
                InviteTable.selectAll()
                    .where { InviteTable.userId.eq(userId) and InviteTable.type.eq(InviteStatus.TO_USER) }
                    .map(::resultRowToInvite)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectByUserId(userId: Int): List<InviteModel> {
        return try {
            transaction {
                InviteTable.selectAll()
                    .where { InviteTable.userId.eq(userId) }
                    .map(::resultRowToInvite)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectById(id: Int): InviteModel? {
        return try {
            transaction {
                InviteTable.selectAll()
                    .where { InviteTable.id.eq(id) }
                    .single()
                    .let(::resultRowToInvite)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun delete(id: Int): Boolean {
        return try {
            transaction {
                InviteTable.deleteWhere(op = { InviteTable.id.eq(id) }) > 0
            }
        } catch (e: Exception) {
            false
        }
    }
}
