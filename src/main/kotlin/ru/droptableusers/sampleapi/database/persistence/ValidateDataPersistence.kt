package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.database.schema.ValidateDataTable

class ValidateDataPersistence {
    fun insert(
        firstName: String,
        lastName: String,
        birthdayDate: Long,
        email: String,
    ) {
        try {
            transaction {
                ValidateDataTable.insert {
                    it[ValidateDataTable.firstName] = firstName
                    it[ValidateDataTable.lastName] = lastName
                    it[ValidateDataTable.birthdayDate] = birthdayDate
                    it[ValidateDataTable.email] = email
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(
        firstName: String,
        lastName: String,
        birthdayDate: Long,
        email: String,
    ): Boolean {
        return try {
            transaction {
                ValidateDataTable.deleteWhere {
                    ValidateDataTable.firstName.eq(firstName) and
                        ValidateDataTable.lastName.eq(lastName) and
                        ValidateDataTable.birthdayDate.eq(birthdayDate) and
                        ValidateDataTable.email.eq(email)
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun validate(
        firstName: String,
        lastName: String,
        birthdayDate: Long,
        email: String,
    ): Boolean {
        return try {
            transaction {
                ValidateDataTable.selectAll()
                    .where {
                        ValidateDataTable.firstName.eq(firstName)
                    }.andWhere {
                        ValidateDataTable.lastName.eq(lastName)
                    }.andWhere {
                        ValidateDataTable.birthdayDate.eq(birthdayDate)
                    }.andWhere {
                        ValidateDataTable.email.eq(email)
                    }.map { it }.isNotEmpty()
            }
        } catch (e: Exception) {
            false
        }
    }
}
