package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TourModel
import ru.droptableusers.sampleapi.data.models.base.TourResultModel
import ru.droptableusers.sampleapi.database.schema.ToursResultTable
import ru.droptableusers.sampleapi.database.schema.ToursTable
import ru.droptableusers.sampleapi.database.schema.UserTable

class ToursPersistence {
    private fun resultRowToTour(row: ResultRow): TourModel =
        TourModel(
            name = row[ToursTable.name],
            year = row[ToursTable.year],
            maxScore = row[ToursTable.maxScore],
        )

    private fun tourResultRowToTourResult(row: ResultRow): TourResultModel =
        TourResultModel(
            tourId = row[ToursResultTable.tourId].value,
            result = row[ToursResultTable.result],
            userId = row[ToursResultTable.userId].value,
        )

    fun insertTour(tourModel: TourModel) {
        try {
            transaction {
                ToursTable.insert {
                    it[ToursTable.name] = tourModel.name
                    it[ToursTable.year] = tourModel.year
                    it[ToursTable.maxScore] = tourModel.maxScore
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun insertResult(tourResultModel: TourResultModel) {
        try {
            transaction {
                ToursResultTable.insert {
                    it[ToursResultTable.tourId] = tourResultModel.tourId
                    it[ToursResultTable.result] = tourResultModel.result
                    it[ToursResultTable.userId] = tourResultModel.userId
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectTourById(tourId: Int): TourModel? {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { ToursTable.id.eq(tourId) }
                    .single()
                    .let(::resultRowToTour)
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun selectResultsByUserId(userId: Int): List<TourResultModel> {
        return try {
            transaction {
                ToursResultTable.selectAll()
                    .where { ToursResultTable.userId.eq(userId) }
                    .map(::tourResultRowToTourResult)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun selectResultsByTourId(tourId: Int): List<TourResultModel> {
        return try {
            transaction {
                ToursResultTable.selectAll()
                    .where { ToursResultTable.tourId.eq(tourId) }
                    .map(::tourResultRowToTourResult)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun listAllTours(): List<TourModel> {
        return try {
            transaction {
                ToursTable.selectAll()
                    .map(::resultRowToTour)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun deleteTourResult(
        userId: Int,
        tourId: Int,
    ): Boolean {
        return try {
            transaction {
                ToursResultTable.deleteWhere { ToursResultTable.userId.eq(userId) and ToursResultTable.tourId.eq(tourId) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }
}
