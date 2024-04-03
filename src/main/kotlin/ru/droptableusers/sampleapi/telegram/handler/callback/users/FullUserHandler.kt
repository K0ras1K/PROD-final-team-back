package ru.droptableusers.sampleapi.telegram.handler.callback.users

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.FullUserKeyboard
import ru.droptableusers.sampleapi.utils.DateUtils

class FullUserHandler(val callbackQuery: CallbackQuery) {
    fun handle(){
        val userId = callbackQuery.data().split("-")[2].toInt()
        val userData = UserPersistence().selectById(userId)!!

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                """
                ${userData.firstName} ${userData.lastName}
                Дата регистрации: ${DateUtils.getCurrentDateAsString(userData.regTime)}
                Направление: ${userData.major?.localizedName ?: "Нет"}
                Дата рождения: ${DateUtils.getCurrentDateAsString(userData.birthdayDate)}
                """.trimIndent()
            ).replyMarkup(FullUserKeyboard.generateFullUserKeyboard(userId))
        )
    }
}