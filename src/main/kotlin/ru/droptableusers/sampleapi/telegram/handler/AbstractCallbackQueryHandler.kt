package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.CallbackQuery
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

abstract class AbstractCallbackQueryHandler(callbackQuery: CallbackQuery) {
    val userData = UserPersistence().selectByTelegramId("@${callbackQuery.message().chat().username()}")!!
}
