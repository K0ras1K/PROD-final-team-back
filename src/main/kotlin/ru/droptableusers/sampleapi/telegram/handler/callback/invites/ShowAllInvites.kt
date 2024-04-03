package ru.droptableusers.sampleapi.telegram.handler.callback.invites

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler
import ru.droptableusers.sampleapi.telegram.keyboard.InvitesKeyboard
import ru.droptableusers.sampleapi.telegram.models.base.InviteBotModel

class ShowAllInvites(val callbackQuery: CallbackQuery): AbstractCallbackQueryHandler(callbackQuery) {
    fun handle(){

        val from = TeamsPersistence().selectById(TeamsPersistence().selectByUserId(userData.id)!!)
        val invites = InvitePersistence().selectByTeamId(from!!.id, InviteStatus.TO_TEAM).map{
            InviteBotModel("${userData.firstName} ${userData.lastName}", it.type, it.teamId, it.id)
        }

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                "Заявки",
            ).replyMarkup(InvitesKeyboard.generateInviteKeyboard(invites)),
        )
    }
}