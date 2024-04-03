package ru.droptableusers.sampleapi.telegram.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.GetFile
import com.pengrad.telegrambot.request.SendMessage
import io.ktor.http.content.*
import org.apache.commons.io.FileUtils
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.FilledDocumentModel
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.states.StateMachine
import java.io.File
import java.util.*

class FilesHandler(val message: Message) {
    fun handle() {
        val fileId = message.document().fileId()
        val user = UserPersistence().selectByTelegramId("@${message.chat().username()}")!!
        val request = GetFile(fileId)
        val respond = TelegramChat.VERIFICATION.BOT.execute(
            request
        )
        val fileByteArray = TelegramChat.VERIFICATION.BOT.getFileContent(respond.file())
        val fileUUID = UUID.randomUUID()

        val fileName = message.document().fileName()
        val fileExt = fileName.split(".").last()
        val filePath = "private/${UserPersistence().selectByTelegramId("@${message.chat().username()}")!!.id},$fileUUID.$fileExt"
        FileUtils.writeByteArrayToFile(File(filePath), fileByteArray)

        DocumentsPersistence().insertFilledDocument(
            FilledDocumentModel(
                id = 0,
                userId = user.id,
                documentId = StateMachine.getStatus(message.chat().id())!!.data[0].toInt(),
                fileName = "${user.id},$fileUUID.$fileExt",
                lastUpdate = System.currentTimeMillis()
            )
        )

        println(fileByteArray)
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                message.chat().id(),
                "Вы успешно загрузили файл!",
            ),
        )

    }
}
