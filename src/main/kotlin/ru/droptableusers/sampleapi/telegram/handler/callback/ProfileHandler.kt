package ru.droptableusers.sampleapi.telegram.handler.callback

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.utils.KeyboardUtils
import ru.droptableusers.sampleapi.utils.DateUtils

class ProfileHandler(val callbackQuery: CallbackQuery) {
    companion object {
        const val CALLBACK_QUERY = "show-profile"
    }

    fun handle() {
        val userData = UserPersistence().selectByTelegramId("@${callbackQuery.message().chat().username()}")!!

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                """
                Ваш профиль, ${userData.firstName} ${userData.lastName}
                Дата регистрации: ${DateUtils.getCurrentDateAsString(userData.regTime)}
                Направление: ${userData.major?.localizedName ?: "Нет"}
                Дата рождения: ${DateUtils.getCurrentDateAsString(userData.birthdayDate)}
                Почта: ${userData.username}
                """.trimIndent(),
            ).replyMarkup(KeyboardUtils.buildBackButton("main")),
        )
    }
}
