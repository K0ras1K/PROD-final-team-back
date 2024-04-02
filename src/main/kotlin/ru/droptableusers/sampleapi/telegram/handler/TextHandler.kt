package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.TelegramModel
import ru.droptableusers.sampleapi.database.persistence.TelegramPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.states.StateMachine
import ru.droptableusers.sampleapi.telegram.templates.MainMessage

class TextHandler(val message: Message) {
    fun handle() {
        if (message.text() == "/start") {
            StateMachine.removeStatus(message.chat().id())
            try {
                val userId =
                    UserPersistence().selectByTelegramId(
                        "@${
                            message.chat().username()
                        }",
                    )!!.id
                if (TelegramPersistence().selectByUserId(userId) == null
                ) {
                    TelegramPersistence().insert(
                        TelegramModel(
                            userId = userId,
                            telegramId = message.chat().id(),
                        ),
                    )
                }
                MainMessage(message.chat().id(), message.chat().username()).create()
            } catch (exception: Exception) {
                MainMessage(message.chat().id(), message.chat().username()).createNotRegistered()
            }
        }
        if (message.text().startsWith("/notify")) {
            val text = message.text().replace("/notify ", "")
            TelegramPersistence().selectAll().forEach {
                TelegramChat.VERIFICATION.BOT.execute(
                    SendMessage(
                        it.telegramId,
                        text,
                    ),
                )
            }
        }
    }
}
