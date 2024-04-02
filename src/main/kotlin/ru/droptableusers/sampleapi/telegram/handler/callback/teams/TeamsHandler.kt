package ru.droptableusers.sampleapi.telegram.handler.callback.teams

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.TeamsKeyboard
import ru.droptableusers.sampleapi.telegram.models.base.TeamBotModel

class TeamsHandler(val callbackQuery: CallbackQuery) {
    companion object {
        const val CALLBACK_QUERY = "show-teams"
    }

    fun handle() {
        val allTeams =
            TeamsPersistence().selectAll(30, 0).map {
                TeamBotModel(
                    id = it.id,
                    name = it.name,
                    membersCount = TeamsPersistence().selectTeammates(it.id).size,
                )
            }
        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                "Доступные команды",
            ).replyMarkup(TeamsKeyboard.generateTeamKeyboard(allTeams)),
        )
    }
}
