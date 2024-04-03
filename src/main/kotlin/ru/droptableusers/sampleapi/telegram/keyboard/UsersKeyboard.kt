package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.TeamsHandler
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils
import ru.droptableusers.sampleapi.telegram.models.base.UserBotModel

object UsersKeyboard {
    fun generateUsersKeyboard(lst: List<UserBotModel>): InlineKeyboardMarkup{
        val onLineCount = 3
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (user in lst) {
            counter += 1
            tempRows +=
                InlineKeyboardButton(
                    "${user.firstName} ${user.lastName}",
                ).callbackData("show-user-${user.id}")
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }
        buttons += mutableListOf(mutableListOf(KeyboardUtils.getBackButton("main")))
        // Convert the MutableList to Array
        val buttonsArray: Array<Array<InlineKeyboardButton>> = buttons.map { it.toTypedArray() }.toTypedArray()

        return InlineKeyboardMarkup(*buttonsArray)
    }
}