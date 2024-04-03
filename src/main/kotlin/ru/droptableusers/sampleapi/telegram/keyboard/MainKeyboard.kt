package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup

object MainKeyboard {
    fun generate(team: Boolean): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            arrayOf(
                InlineKeyboardButton("\uD83E\uDDD1\u200D\uD83D\uDCBC Мой профиль").callbackData("show-profile"),
            ),
            arrayOf(
                InlineKeyboardButton("Команды").callbackData("show-teams"),
            ),
            arrayOf(
                InlineKeyboardButton("Пользователи").callbackData("show-users"),
            ),
            arrayOf(
                InlineKeyboardButton("Документы").callbackData("show-documents"),
            ),
            if (team) {
                arrayOf(
                    InlineKeyboardButton("Моя команда").callbackData("show-my-team"),
                )
            } else {
                arrayOf()
            },

            if (team){
                arrayOf(
                    InlineKeyboardButton("Мои заявки").callbackData("show-all-invites"),
                )
            } else {
                arrayOf()
            }
        )
    }
}
