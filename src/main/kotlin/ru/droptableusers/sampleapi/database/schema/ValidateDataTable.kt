package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.Table

object ValidateDataTable : Table("validate_data") {
    val firstName = varchar("firstName", 64)
    val lastName = varchar("lastName", 64)
    val birthdayDate = long("birthdayDate")
    val email = varchar("email", 256)
}
