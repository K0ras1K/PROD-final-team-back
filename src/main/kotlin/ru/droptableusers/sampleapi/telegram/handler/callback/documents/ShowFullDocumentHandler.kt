package ru.droptableusers.sampleapi.telegram.handler.callback.documents

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler

class ShowFullDocumentHandler(val callbackQuery: CallbackQuery): AbstractCallbackQueryHandler(callbackQuery) {
    fun handle() {
        val documentId = callbackQuery.data().split("-")[2].toInt()
        val documentData = DocumentsPersistence().selectDocumentById(documentId)

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                """
                    ${documentData!!.name}
                    ${documentData.description}
                """.trimIndent()
            )
        )
    }
}