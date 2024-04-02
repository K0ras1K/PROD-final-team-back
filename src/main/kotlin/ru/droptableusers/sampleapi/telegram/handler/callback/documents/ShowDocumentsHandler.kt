package ru.droptableusers.sampleapi.telegram.handler.callback.documents

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.DocumentConditionModel
import ru.droptableusers.sampleapi.data.models.base.DocumentModel
import ru.droptableusers.sampleapi.data.models.base.FilledDocumentModel
import ru.droptableusers.sampleapi.database.persistence.DocumentsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler
import ru.droptableusers.sampleapi.telegram.keyboard.DocumentsKeyboard
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ShowDocumentsHandler(val callbackQuery: CallbackQuery) : AbstractCallbackQueryHandler(callbackQuery) {
    fun handle() {
        val documents = getFilledAndUnfilledDocumentsForUser(userData.id)
        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                "Менеджер документов",
            ).replyMarkup(DocumentsKeyboard.generateDocumentsKeyboard(documents)),
        )
    }

    private fun getFilledAndUnfilledDocumentsForUser(userId: Int): Pair<List<DocumentModel>, List<DocumentModel>> {
        val documents = DocumentsPersistence().listDocuments()
        val conditions = compareDocumentAndConditions(DocumentsPersistence().listDocumentConditions())
        val filledDocuments = compareUserAndFilledDocuments(DocumentsPersistence().listFilledDocuments())

        val filledDocs = mutableListOf<DocumentModel>()
        val unfilledDocs = mutableListOf<DocumentModel>()

        val user = UserPersistence().selectById(userData.id)

        documents.forEach { doc ->
            println(doc)
            if (doc.required) {
                println("doc req")
                val documentConditions = conditions[doc.id].orEmpty()
                var isRequired = false
                var isFilled = false

                documentConditions.forEach { condition ->
                    println(condition)
                    when (condition.fieldName) {
                        "age" -> {
                            val age = unixToLocalDateTime(user?.birthdayDate!!).until(unixToLocalDateTime(System.currentTimeMillis()), ChronoUnit.YEARS)
                            when (condition.condition) {
                                "less" -> {
                                    if (age < condition.value.toInt()) {
                                        isRequired = true
                                    }
                                }
                                "equals" -> {
                                    if (age == condition.value.toLong()) {
                                        isRequired = true
                                    }
                                }
                                "more" -> {
                                    println("$age ${condition.value}")
                                    if (age > condition.value.toInt()) {
                                        isRequired = true
                                    }
                                }
                            }
                        }
                        "sex" -> {
                            val sex = "male"; // TODO Fetch from DB
                            if (sex == condition.value) {
                                isRequired = true
                            }
                        }
                    }
                }

                if (isRequired) {
                    if (filledDocuments.containsKey(userId) && filledDocuments[userId]!!.containsKey(doc.id)) {
                        isFilled = true
                    }

                    if (isFilled) {
                        filledDocs.add(doc)
                    } else {
                        unfilledDocs.add(doc)
                    }
                }
            }
        }

        return Pair(filledDocs, unfilledDocs)
    }

    private fun compareDocumentAndConditions(conditions: List<DocumentConditionModel>): Map<Int, List<DocumentConditionModel>> {
        val result = HashMap<Int, MutableList<DocumentConditionModel>>()
        conditions.forEach {
            if (result.containsKey(it.documentId) && result[it.documentId] != null) {
                result[it.documentId]!!.add(it)
            } else {
                result[it.documentId] = mutableListOf(it)
            }
        }
        return result
    }

    private fun compareUserAndFilledDocuments(filledDocuments: List<FilledDocumentModel>): Map<Int, Map<Int, FilledDocumentModel>> {
        val result = HashMap<Int, MutableMap<Int, FilledDocumentModel>>()
        filledDocuments.forEach {
            if (result.containsKey(it.userId) && result[it.userId] != null) {
                result[it.userId]!![it.documentId] = it
            } else {
                result[it.userId] = mutableMapOf(Pair(it.documentId, it))
            }
        }
        return result
    }

    private fun unixToLocalDateTime(time: Long): LocalDate {
        return Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}
