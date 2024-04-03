package ru.droptableusers.sampleapi.telegram.handler.callback.invites

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.InviteStatus
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler
import ru.droptableusers.sampleapi.telegram.keyboard.InvitesKeyboard
import ru.droptableusers.sampleapi.telegram.keyboard.TeamsKeyboard
import ru.droptableusers.sampleapi.telegram.models.base.InviteBotModel

class ShowAllInvites(val callbackQuery: CallbackQuery): AbstractCallbackQueryHandler(callbackQuery) {
    fun handle(){
        val invites = InvitePersistence().selectByUserId(userData.id).map{
            val from = if(it.type == InviteStatus.TO_USER)
                TeamsPersistence().selectById(it.teamId)!!.name
            else
                UserPersistence().selectById(it.userId)!!.let { i -> "${i.firstName} ${i.lastName}" }
            InviteBotModel(from, it.type, it.teamId, it.id)
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