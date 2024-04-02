package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.GetFile
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import java.io.File

class FilesHandler(val message: Message) {
    fun handle() {
        val fileId = message.document().fileId()
        val request = GetFile(fileId)
        val respond = TelegramChat.VERIFICATION.BOT.execute(
            request
        )
        val fileByteArray = TelegramChat.VERIFICATION.BOT.getFileContent(respond.file())
        println(fileByteArray)
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                message.chat().id(),
                "Вы успешно загрузили файл!",
            ),
        )

    }
}
