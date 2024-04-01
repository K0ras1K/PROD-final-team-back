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
}
