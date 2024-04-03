package ru.droptableusers.sampleapi.telegram.handler.callback.users

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.SendMessage
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.InviteModel
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler

class ApplyUserHandler(val callbackQuery: CallbackQuery) : AbstractCallbackQueryHandler(callbackQuery) {
    fun handle() {
        val userId = callbackQuery.data().split("-")[2].toInt()
        InvitePersistence().insert(
            InviteModel(
                id = 0,
                teamId = TeamsPersistence().selectByUserId(userId) ?: -1,
                userId = userId,
                type = InviteStatus.TO_USER,
            ),
        )
        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                callbackQuery.message().chat().id(),
                "Вы успешно отправили приглашение на вступление!",
            ),
        )
    }
}
