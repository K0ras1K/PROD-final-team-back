package ru.droptableusers.sampleapi.telegram.handler.callback.teams

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils

class FullTeamHandler(val callbackQuery: CallbackQuery) {
    fun handle() {
        val teamId = callbackQuery.data().split("-")[2].toInt()
        val teamData = TeamsPersistence().selectById(teamId)!!

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                """
                Команда ${teamData.name}
                Описание: ${teamData.description}
                """.trimIndent(),
            ).replyMarkup(KeyboardUtils.buildBackButton(TeamsHandler.CALLBACK_QUERY)),
        )
    }
}
