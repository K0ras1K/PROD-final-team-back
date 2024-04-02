package ru.droptableusers.sampleapi.telegram.handler.callback.teams

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler

class ApplyTeamHandler(val callbackQuery: CallbackQuery) : AbstractCallbackQueryHandler(callbackQuery) {
    fun handle() {
        val teamId = callbackQuery.data().split("-")[2].toInt()
        InvitePersistence().insert(
            InviteModel(
                id = 0,
                teamId = teamId,
                userId = userData.id,
                type = InviteStatus.TO_TEAM,
            ),
        )
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                callbackQuery.message().chat().id(),
                "Вы успешно подали заявку на вступление!",
            ),
        )
    }
}
