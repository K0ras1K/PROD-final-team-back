package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.GroupModel
import ru.droptableusers.sampleapi.database.schema.GroupTable

class GroupPersistence {
    fun select(username: String): GroupModel? {
        return try {
            transaction {
                GroupTable.selectAll()
                    .where { GroupTable.username.eq(username) }
                    .single()
                    .let {
                        GroupModel(
                            username = username,
                            group = it[GroupTable.group]
                        )
                    }
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun insert(groupModel: GroupModel) {
        try {
            transaction {
                GroupTable.insert {
                    it[GroupTable.username] = groupModel.username
                    it[GroupTable.group] = groupModel.group
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}