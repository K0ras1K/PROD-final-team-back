package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.database.schema.TagsUsersTable
import ru.droptableusers.sampleapi.database.schema.UserTable

/**
 * User persistence
 *
 * @property username
 * @constructor Create empty User persistence
 *
 * @author Roman K0ras1K Kalmykov
 */
class UserPersistence {
    private fun resultRowToUserModel(row: ResultRow): UserModel =
        UserModel(
            username = row[UserTable.username],
            password = row[UserTable.password],
            regTime = row[UserTable.regTime],
            tgLogin = row[UserTable.tgLogin],
            firstName = row[UserTable.firstName],
            lastName = row[UserTable.lastName],
            birthdayDate = row[UserTable.birthdayDate],
            id = row[UserTable.id].value,
            description = row[UserTable.description],
            major = row[UserTable.major],
        )

    /**
     * Insert
     *
     * @param userModel
     * @author Roman K0ras1K Kalmykov
     */
    fun insert(userModel: UserModel): UserModel? {
        return try {
            transaction {
                UserTable.insert {
                    it[UserTable.username] = userModel.username
                    it[UserTable.password] = userModel.password
                    it[UserTable.firstName] = userModel.firstName
                    it[UserTable.lastName] = userModel.lastName
                    it[UserTable.birthdayDate] = userModel.birthdayDate
                    it[UserTable.tgLogin] = userModel.tgLogin
                    it[UserTable.regTime] = userModel.regTime
                    it[UserTable.description] = userModel.description
                    it[UserTable.major] = userModel.major
                }.resultedValues!!.single().let(
                    ::resultRowToUserModel,
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    /**
     * Select by username
     *
     * @return UserModel
     */
    fun selectByUsername(username: String): UserModel? {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { UserTable.username.eq(username) }
                    .single()
                    .let(::resultRowToUserModel)
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun selectById(id: Int): UserModel? {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { UserTable.id.eq(id) }
                    .single()
                    .let(::resultRowToUserModel)
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun selectByTelegramId(id: String): UserModel? {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { UserTable.tgLogin.eq(id) }
                    .single()
                    .let(::resultRowToUserModel)
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun update(userModel: UserModel): Boolean {
        return try {
            transaction {
                UserTable.update({ UserTable.username eq userModel.username }) {
                    it[UserTable.password] = userModel.password
                    it[UserTable.firstName] = userModel.firstName
                    it[UserTable.lastName] = userModel.lastName
                    it[UserTable.birthdayDate] = userModel.birthdayDate
                    it[UserTable.tgLogin] = userModel.tgLogin
                    it[UserTable.regTime] = userModel.regTime
                    it[UserTable.description] = userModel.description
                } > 0
            }
        } catch (exception: Exception) {
            false
        }
    }

    fun delete(username: String): Boolean {
        return try {
            transaction {
                UserTable.deleteWhere(op = { UserTable.username eq username }) > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun selectByIdList(idList: List<Int>): List<UserModel> {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { UserTable.id.inList(idList) }
                    .map(::resultRowToUserModel)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun selectTagIds(userId: Int): List<Int> {
        return try {
            transaction {
                TagsUsersTable.selectAll()
                    .where { TagsUsersTable.userId eq userId }
                    .map {
                        it[TagsUsersTable.tagId].value
                    }
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun containsTagId(
        userId: Int,
        tagId: Int,
    ): Boolean {
        return try {
            transaction {
                TagsUsersTable.selectAll()
                    .where { TagsUsersTable.userId.eq(userId) }
                    .andWhere { TagsUsersTable.tagId.eq(tagId) }
            }.count() > 0
        } catch (e: Exception) {
            false
        }
    }

    fun allUsersWithoutTeam(
        limit: Int,
        offset: Long,
    ): List<UserModel> {
        return try {
            transaction {
                UserTable.selectAll()
                    .where { UserTable.id.notInList(TeamsPersistence().selectAllMembers()) }
                    .limit(limit, offset = offset)
                    .map(::resultRowToUserModel)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun listUsers(): List<UserModel> {
        return try {
            transaction {
                UserTable.selectAll()
                    .map(::resultRowToUserModel)
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun addTag(
        userId: Int,
        tagId: Int,
    ) {
        try {
            transaction {
                TagsUsersTable.insert {
                    it[TagsUsersTable.id] = "$tagId/$userId"
                    it[TagsUsersTable.tagId] = tagId
                    it[TagsUsersTable.userId] = userId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeTag(
        userId: Int,
        tagId: Int,
    ): Boolean {
        return try {
            transaction {
                TagsUsersTable.deleteWhere {
                    TagsUsersTable.userId.eq(userId) and
                        TagsUsersTable.tagId.eq(tagId)
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }
}
