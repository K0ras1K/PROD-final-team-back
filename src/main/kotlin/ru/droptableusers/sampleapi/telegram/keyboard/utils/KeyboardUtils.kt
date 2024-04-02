package ru.droptableusers.sampleapi.telegram.keyboard.utils

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup

object KeyboardUtils {
    fun getBackButton(query: String): InlineKeyboardButton {
        return InlineKeyboardButton("\uD83D\uDD19 Назад").callbackData(query)
    }

    fun buildBackButton(query: String): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            arrayOf(getBackButton(query)),
        )
    }
}
