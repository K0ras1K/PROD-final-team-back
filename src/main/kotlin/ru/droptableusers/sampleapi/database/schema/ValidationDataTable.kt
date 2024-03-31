package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable

object ValidationDataTable : IntIdTable("validation_table"){
    val firstName = varchar("firstName", 32)
    val lastName = varchar("lastName", 32)
    val birthdayDate = long("birthdayDate")
    val email = varchar("email", 512)
}