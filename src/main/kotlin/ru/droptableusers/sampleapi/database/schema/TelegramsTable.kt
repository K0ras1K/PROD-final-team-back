package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TelegramsTable : Table("telegrams") {
    val userId = reference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val telegramId = long("telegramId")
}
