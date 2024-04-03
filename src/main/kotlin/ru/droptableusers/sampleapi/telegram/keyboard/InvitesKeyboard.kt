package ru.droptableusers.sampleapi.telegram.keyboard

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils
import ru.droptableusers.sampleapi.telegram.models.base.InviteBotModel
import ru.droptableusers.sampleapi.telegram.models.base.TeamBotModel

object InvitesKeyboard {
    fun generateInviteKeyboard(invites: List<InviteBotModel>): InlineKeyboardMarkup {
        val onLineCount = 1
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (inv in invites) {
            val text = if (inv.type == InviteStatus.TO_USER) "Приглашение к ${inv.from}" else "Запрос от ${inv.from}"
            val data = if (inv.type == InviteStatus.TO_USER) "accept-user-${inv.id}" else "accept-team-${inv.teamId}"
            counter += 1
            tempRows +=
                InlineKeyboardButton(
                    text,
                ).callbackData(data)
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