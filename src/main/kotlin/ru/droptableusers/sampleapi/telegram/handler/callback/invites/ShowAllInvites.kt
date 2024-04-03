package ru.droptableusers.sampleapi.telegram.handler.callback.invites

import com.pengrad.telegrambot.model.CallbackQuery
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler

class ShowAllInvites(val callbackQuery: CallbackQuery): AbstractCallbackQueryHandler(callbackQuery) {
    fun handle(){
        val invites = InvitePersistence().selectByUserId(userData.id)


    }
}