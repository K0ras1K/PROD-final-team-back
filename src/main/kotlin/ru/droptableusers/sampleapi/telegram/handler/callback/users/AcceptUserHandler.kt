package ru.droptableusers.sampleapi.telegram.handler.callback.users

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.SendMessage
import io.ktor.http.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.InvitePersistence
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler

class AcceptUserHandler(val callbackQuery: CallbackQuery): AbstractCallbackQueryHandler(callbackQuery) {
    fun handle(){
        val inviteId = callbackQuery.data().split("-")[2].toInt()
        val inviteData = InvitePersistence().selectById(inviteId)!!

        InvitePersistence().delete(inviteId)
        TeamsPersistence().addMember(inviteData.userId, inviteData.teamId)
        val user = UserPersistence().selectById(inviteData.userId)
        if (user?.major != null){
            val sfm = SearchingForPersistence().selectFirstByMajor(user.major)
            if (sfm != null) SearchingForPersistence().deleteBySlotIndex(inviteData.teamId, sfm.slotIndex)
        }

        TelegramChat.VERIFICATION.BOT.execute(
            SendMessage(
                callbackQuery.message().chat().id(),
                "Вы успешно обьединились с командой",
            ),
        )
    }
}