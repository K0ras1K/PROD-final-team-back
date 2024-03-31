package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TourResultModel
import ru.droptableusers.sampleapi.database.schema.ToursResultTable

class ToursResultPersistence {

    private fun resultRowToTourResult(row: ResultRow): TourResultModel =
        TourResultModel(
            name = row[ToursResultTable.name],
            result = row[ToursResultTable.result],
            userId = row[ToursResultTable.userId].value
        )

    fun insert(tourResultModel: TourResultModel) {
        try {
            transaction {
                ToursResultTable.insert {
                    it[ToursResultTable.name] = tourResultModel.name
                    it[ToursResultTable.result] = ToursResultTable.result
                    it[ToursResultTable.userId] = ToursResultTable.userId
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectByUserId(userId: Int): TourResultModel? {
        return try {
            transaction {
                ToursResultTable.selectAll()
                    .where { ToursResultTable.userId.eq(userId) }
                    .single()
                    .let(::resultRowToTourResult)
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun delete(userId: Int, tourName: String): Boolean {
        return try {
            transaction {
                ToursResultTable.deleteWhere { ToursResultTable.userId.eq(userId) and ToursResultTable.name.eq(tourName) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

}