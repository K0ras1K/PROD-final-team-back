package ru.droptableusers.sampleapi.tasks

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup

object Keyboard {
    fun generateVerificationKeyboard(id: Int): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            arrayOf<InlineKeyboardButton>(
                InlineKeyboardButton("✅ Верицифировать").callbackData("verif-$id"),
            ),
        )
    }
}
