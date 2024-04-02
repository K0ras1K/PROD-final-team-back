package ru.droptableusers.sampleapi.telegram.handler.callback.documents

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import ru.droptableusers.sampleapi.telegram.enums.TextStatus
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils
import ru.droptableusers.sampleapi.telegram.models.StatusData
import ru.droptableusers.sampleapi.telegram.states.StateMachine

class ShowFullDocumentHandler(val callbackQuery: CallbackQuery): AbstractCallbackQueryHandler(callbackQuery) {
    fun handle() {
        val documentId = callbackQuery.data().split("-")[2].toInt()
        val documentData = DocumentsPersistence().selectDocumentById(documentId)

        StateMachine.setStatus(
            callbackQuery.message().chat().id(),
            StatusData(
                status = TextStatus.UPLOAD_FILE,
                headMessage = callbackQuery.message().messageId().toLong(),
                data = mutableListOf(documentId.toString())
            )
        )

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                """
                    ${documentData!!.name}
                    ${documentData.description}
                    Допустимые форматы файла: ${documentData.extensions}
                """.trimIndent()
            ).replyMarkup(KeyboardUtils.buildBackButton("show-documents"))
        )
    }
}