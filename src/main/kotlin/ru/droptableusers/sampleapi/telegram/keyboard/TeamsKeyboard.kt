package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils.getBackButton
import ru.droptableusers.sampleapi.telegram.models.base.TeamBotModel

object TeamsKeyboard {
    fun generateTeamKeyboard(teams: List<TeamBotModel>): InlineKeyboardMarkup {
        val onLineCount = 2
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (team in teams) {
            counter += 1
            tempRows +=
                InlineKeyboardButton(
                    "[${team.membersCount}] ${team.name}",
                ).callbackData("show-team-${team.id}")
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }
        buttons += mutableListOf(mutableListOf(getBackButton("main")))
        // Convert the MutableList to Array
        val buttonsArray: Array<Array<InlineKeyboardButton>> = buttons.map { it.toTypedArray() }.toTypedArray()

        return InlineKeyboardMarkup(*buttonsArray)
    }
}
