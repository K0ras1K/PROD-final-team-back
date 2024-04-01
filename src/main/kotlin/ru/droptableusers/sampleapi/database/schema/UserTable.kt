package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.IntIdTable
import ru.droptableusers.sampleapi.data.enums.Major

/**
 * User table
 *
 * @constructor Create empty User table
 * @author Roman K0ras1K Kalmykov
 */
object UserTable : IntIdTable("users") {
    val username = varchar("username", 256)
    val password = varchar("password", 200)
    val tgLogin = varchar("tg_login", 100)
    val firstName = varchar("first_name", 40)
    val lastName = varchar("last_name", 50)
    val birthdayDate = long("birthday_date")
    val major = enumerationByName("major", 20, Major::class).nullable()
    val regTime = long("reg_time")
    val description = text("description")
}
