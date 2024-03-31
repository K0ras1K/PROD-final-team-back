package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.database.schema.UserTable

/**
 * User persistence
 *
 * @property username
 * @constructor Create empty User persistence
 *
 * @author Roman K0ras1K Kalmykov
 */
class UserPersistence(val username: String) {
    /**
     * Insert
     *
     * @param userModel
     * @author Roman K0ras1K Kalmykov
     */
    fun insert(userModel: UserModel) {
        try {
            transaction {
                UserTable.insert {
                    it[UserTable.username] = userModel.username
                    it[UserTable.password] = userModel.password
                    it[UserTable.firstName] = userModel.firstName
                    it[UserTable.lastName] = userModel.lastName
                    it[UserTable.birthdayDate] = userModel.birthdayDate
                    it[UserTable.tgLogin] = userModel.tgLogin
                    it[UserTable.regTime] = userModel.regTime
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     * Select by username
     *
     * @return UserModel
     */
    fun selectByUsername(): UserModel? {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { UserTable.username.eq(username) }
                    .single()
                    .let {
                        UserModel(
                            username = username,
                            password = it[UserTable.password],
                            regTime = it[UserTable.regTime],
                            tgLogin = it[UserTable.tgLogin],
                            firstName = it[UserTable.firstName],
                            lastName = it[UserTable.lastName],
                            birthdayDate = it[UserTable.birthdayDate],
                        )
                    }
            }
        } catch (exception: Exception) {
            null
        }
    }
}
