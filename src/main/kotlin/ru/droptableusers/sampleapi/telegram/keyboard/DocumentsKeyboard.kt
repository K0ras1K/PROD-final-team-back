package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import ru.droptableusers.sampleapi.data.models.base.DocumentModel
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils

object DocumentsKeyboard {
    fun generateDocumentsKeyboard(documents: Pair<List<DocumentModel>, List<DocumentModel>>): InlineKeyboardMarkup {
        val onLineCount = 2
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (doc in documents.first) {
            counter += 1
            tempRows +=
                InlineKeyboardButton(
                    "✅ ${doc.name}",
                ).callbackData("docc")
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }

        tempRows = mutableListOf()
        counter = 0
        for (doc in documents.second) {
            counter += 1
            tempRows +=
                InlineKeyboardButton(
                    "❌ ${doc.name}",
                ).callbackData("show-doc-${doc.id}")
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }

        buttons += mutableListOf(mutableListOf(KeyboardUtils.getBackButton("main")))

        val buttonsArray: Array<Array<InlineKeyboardButton>> = buttons.map { it.toTypedArray() }.toTypedArray()

        return InlineKeyboardMarkup(*buttonsArray)
    }
}