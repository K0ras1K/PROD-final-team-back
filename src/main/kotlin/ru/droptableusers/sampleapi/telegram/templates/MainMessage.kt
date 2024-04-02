package ru.droptableusers.sampleapi.telegram.templates

import com.pengrad.telegrambot.request.EditMessageText
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.MainKeyboard

class MainMessage(val chatId: Long, val userLogin: String) {
    fun create() {
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                chatId,
                """
                Привет! Это бот проекта DropTableUsers
                Готов начинать?
                """.trimIndent(),
            ).replyMarkup(
                MainKeyboard.generate(
                    TeamsPersistence().selectByUserId(
                        UserPersistence().selectByTelegramId("@$userLogin")!!.id,
                    ) != null,
                ),
            ),
        )
    }

    fun createNotRegistered() {
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                chatId,
                """
                Ой! Похоже, вы не зарегистрированы на нашей платформе :(
                Вы можете создать аккаунт здесь - https://web.ethereally.space
                """.trimIndent(),
            ),
        )
    }

    fun replace(messageId: Int) {
        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                chatId,
                messageId,
                """
                Привет! Это бот проекта DropTableUsers
                Готов начинать?
                """.trimIndent(),
            ).replyMarkup(
                MainKeyboard.generate(
                    TeamsPersistence().selectByUserId(
                        UserPersistence().selectByTelegramId("@$userLogin")!!.id,
                    ) != null,
                ),
            ),
        )
    }
}
