package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.Message
import ru.droptableusers.sampleapi.data.models.base.TelegramModel
import ru.droptableusers.sampleapi.database.persistence.TelegramPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.states.StateMachine
import ru.droptableusers.sampleapi.telegram.templates.MainMessage

class TextHandler(val message: Message) {
    fun handle() {
        if (message.text() == "/start") {
            StateMachine.removeStatus(message.chat().id())
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
            MainMessage(message.chat().id()).create()
        }
    }
}
