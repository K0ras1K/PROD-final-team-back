package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.database.schema.InviteTable

class InvitePersistence {
    private fun resultRowToInvite(resultRow: ResultRow) =
        InviteModel(
            teamId = resultRow[InviteTable.teamId].value,
            userId = resultRow[InviteTable.userId].value,
            type = resultRow[InviteTable.type]
        )

    fun insert(inviteModel: InviteModel) {
        try {
            InviteTable.insert {
                it[InviteTable.teamId] = inviteModel.teamId
                it[InviteTable.userId] = inviteModel.userId
                it[InviteTable.type] = type
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectByTeamId(teamId: Int): List<InviteModel> {
        return try {
            InviteTable.selectAll()
                .where { InviteTable.teamId.eq(teamId) }
                .map(::resultRowToInvite)
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectByUserId(userId: Int): List<InviteModel> {
        return try {
            InviteTable.selectAll()
                .where { InviteTable.userId.eq(userId) }
                .map(::resultRowToInvite)
        } catch (e: Exception) {
            listOf()
        }
    }

    fun selectById(id: Int): InviteModel? {
        return try {
            InviteTable.selectAll()
                .where { InviteTable.id.eq(id) }
                .single()
                .let(::resultRowToInvite)
        } catch (e: Exception) {
            null
        }
    }

    fun delete(id: Int): Boolean {
        return try {
            InviteTable.deleteWhere(op = { InviteTable.id.eq(id) }) > 0
        } catch (e: Exception) {
            false
        }
    }
}