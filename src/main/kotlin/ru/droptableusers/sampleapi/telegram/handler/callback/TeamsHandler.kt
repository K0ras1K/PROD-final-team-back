package ru.droptableusers.sampleapi.telegram.handler.callback

import com.pengrad.telegrambot.model.CallbackQuery
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class TeamsHandler(val callbackQuery: CallbackQuery) {
    fun handle() {
        val userId = UserPersistence().selectByTelegramId("@${callbackQuery.from().username()}")
    }
}
