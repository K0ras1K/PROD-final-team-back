package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat

class FilesHandler(val message: Message) {
    fun handle() {
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                message.chat().id(),
                "Вы успешно загрузили файл!",
            ),
        )
    }
}
