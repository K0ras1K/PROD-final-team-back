package ru.droptableusers.sampleapi.telegram.handler.callback

import com.pengrad.telegrambot.model.CallbackQuery
import ru.droptableusers.sampleapi.telegram.templates.MainMessage

class MainHandler(val callbackQuery: CallbackQuery) {
    fun handle() {
        MainMessage(
            callbackQuery.message().chat().id(),
            callbackQuery.message().chat().username(),
        ).replace(callbackQuery.message().messageId())
    }
}
