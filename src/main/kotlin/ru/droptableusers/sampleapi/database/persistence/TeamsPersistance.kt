package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.database.schema.TeamTable
import ru.droptableusers.sampleapi.database.schema.TeamsUsersTable

class TeamsPersistance() {
    private fun resultRowToTeamModel(resultRow: ResultRow)=
        TeamModel(
            id = resultRow[TeamTable.id].value,
            name = resultRow[TeamTable.name],
            description = resultRow[TeamTable.description],
            iconUrl = resultRow[TeamTable.iconUrl],
            bannerUrl = resultRow[TeamTable.bannerUrl]
        )

    fun insert(teamModel: TeamModel){
        try {
            TeamTable.insert {
                it[TeamTable.name] = teamModel.name
                it[TeamTable.description] = teamModel.description
                it[TeamTable.iconUrl] = teamModel.iconUrl
                it[TeamTable.bannerUrl] = teamModel.bannerUrl
            }
        } catch (e: Exception){

        }
    }

    fun selectById(id: Int): TeamModel?{
        return try{
            transaction {
                TeamTable.selectAll()
                    .where{TeamTable.id.eq(id)}
                    .single()
                    .let(::resultRowToTeamModel)
            }
        }
        catch (e: Exception){
            null
        }
    }

    fun update(teamModel: TeamModel): Boolean{
        return try {
            transaction {
                TeamTable.update ({TeamTable.id.eq(teamModel.id)}) {
                    it[TeamTable.name] = teamModel.name
                    it[TeamTable.description] = teamModel.description
                    it[TeamTable.iconUrl] = teamModel.iconUrl
                    it[TeamTable.bannerUrl] = teamModel.bannerUrl
                } > 0
            }
        } catch (e: Exception){
            false
        }
    }

    fun delete(id: Int): Boolean{
        return try {
            transaction {
                TeamTable.deleteWhere { TeamTable.id.eq(id) } > 0
            }
        } catch (e: Exception){
            false
        }
    }

    private fun getTeammateId(resultRow: ResultRow): Int = resultRow[TeamsUsersTable.userId].value

    fun selectTeammates(id: Int): List<Int>{
        return try {
            transaction {
                TeamTable.selectAll()
                    .where{TeamTable.id.eq(id)}
                    .map(::getTeammateId)
            }
        } catch (e: Exception){
            listOf()
        }
    }
}