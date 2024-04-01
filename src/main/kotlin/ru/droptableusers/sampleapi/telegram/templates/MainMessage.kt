package ru.droptableusers.sampleapi.telegram.templates

import com.pengrad.telegrambot.request.EditMessageText
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.telegram.keyboard.MainKeyboard

class MainMessage(val chatId: Long) {
    fun create() {
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                chatId,
                """
                Привет! Это бот проекта DropTableUsers
                Готов начинать?
                """.trimIndent(),
            ).replyMarkup(MainKeyboard.generate()),
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
            ).replyMarkup(MainKeyboard.generate()),
        )
    }
}
