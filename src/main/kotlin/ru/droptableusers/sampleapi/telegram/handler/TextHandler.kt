package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.Message
import ru.droptableusers.sampleapi.telegram.states.StateMachine
import ru.droptableusers.sampleapi.telegram.templates.MainMessage

class TextHandler(val message: Message) {
    fun handle() {
        if (message.text() == "/start") {
            StateMachine.removeStatus(message.chat().id())
            MainMessage(message.chat().id()).create()
        }
    }
}
