package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup

object MainKeyboard {
    fun generate(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            arrayOf(
                InlineKeyboardButton("\uD83E\uDDD1\u200D\uD83D\uDCBC Мой профиль").callbackData("show-profile"),
            ),
            arrayOf(
                InlineKeyboardButton(" Команды").callbackData("show-teams"),
            ),
            arrayOf(
                InlineKeyboardButton("Пользователи").callbackData("show-users"),
            ),
        )
    }
}
