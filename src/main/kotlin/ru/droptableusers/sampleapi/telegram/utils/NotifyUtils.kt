package ru.droptableusers.sampleapi.telegram.utils

import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.TelegramPersistence

object NotifyUtils {
    fun notifyByIds(
        ids: List<Int>,
        text: String,
    ) {
        val tgIds = TelegramPersistence().selectByIds(ids)
        tgIds.forEach {
            TelegramChat.VERIFICATION.BOT.execute(
                SendMessage(
                    it,
                    text,
                ),
            )
        }
    }
}
