package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.TeamsHandler
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils
import ru.droptableusers.sampleapi.telegram.models.base.FullTeamBotModel

object FullUserKeyboard {
    fun generateFullUserKeyboard(userId: Int): InlineKeyboardMarkup {
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()
        buttons += mutableListOf(mutableListOf(InlineKeyboardButton("Подать заявку").callbackData("apply-user-$userId")))
        buttons += mutableListOf(mutableListOf(KeyboardUtils.getBackButton("show-users")))

        val buttonsArray: Array<Array<InlineKeyboardButton>> = buttons.map { it.toTypedArray() }.toTypedArray()

        return InlineKeyboardMarkup(*buttonsArray)
    }
}
