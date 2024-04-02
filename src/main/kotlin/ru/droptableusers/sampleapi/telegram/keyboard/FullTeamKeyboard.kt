package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.TeamsHandler
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils
import ru.droptableusers.sampleapi.telegram.models.base.FullTeamBotModel

object FullTeamKeyboard {
    fun generateFullTeamKeyboard(fullTeamBotModel: FullTeamBotModel): InlineKeyboardMarkup {
        val onLineCount = 1
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (user in fullTeamBotModel.users) {
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
        buttons += mutableListOf(mutableListOf(InlineKeyboardButton("Подать заявку").callbackData("apply-team-${fullTeamBotModel.teamId}")))
        buttons += mutableListOf(mutableListOf(KeyboardUtils.getBackButton(TeamsHandler.CALLBACK_QUERY)))

        val buttonsArray: Array<Array<InlineKeyboardButton>> = buttons.map { it.toTypedArray() }.toTypedArray()

        return InlineKeyboardMarkup(*buttonsArray)
    }
}
