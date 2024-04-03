package ru.droptableusers.sampleapi.telegram.handler.callback.users

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.User
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.UsersKeyboard
import ru.droptableusers.sampleapi.telegram.models.base.UserBotModel

class UsersWithoutTeamHandler(val callbackQuery: CallbackQuery) {
    fun handle(){
        val withoutTeam = UserPersistence()
            .allUsersWithoutTeam(1000, 0).map{
            UserBotModel(
                id = it.id,
                firstName = it.firstName,
                lastName = it.lastName
            )
        }

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                "Участники без команды"
            ).replyMarkup(UsersKeyboard.generateUsersKeyboard(withoutTeam))
        )
    }
}