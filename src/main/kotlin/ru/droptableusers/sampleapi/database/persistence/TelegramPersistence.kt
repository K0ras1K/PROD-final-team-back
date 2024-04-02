package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TelegramModel
import ru.droptableusers.sampleapi.database.schema.TelegramsTable

class TelegramPersistence {
    fun insert(telegramModel: TelegramModel) {
        try {
            transaction {
                TelegramsTable.insert {
                    it[TelegramsTable.userId] = telegramModel.userId
                    it[TelegramsTable.telegramId] = telegramModel.telegramId
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectByUserId(userId: Int): TelegramModel? {
        return try {
            transaction {
                TelegramsTable.selectAll()
                    .where { TelegramsTable.userId.eq(userId) }
                    .single()
                    .let {
                        TelegramModel(
                            userId = userId,
                            telegramId = it[TelegramsTable.telegramId],
                        )
                    }
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun selectAll(): List<TelegramModel> {
        return try {
            transaction {
                TelegramsTable.selectAll()
                    .map {
                        TelegramModel(
                            userId = it[TelegramsTable.userId].value,
                            telegramId = it[TelegramsTable.telegramId],
                        )
                    }
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun selectByIds(ids: List<Int>): List<Long> {
        return try {
            transaction {
                TelegramsTable.selectAll()
                    .where { TelegramsTable.userId.inList(ids) }
                    .map {
                        it[TelegramsTable.telegramId]
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }
}
