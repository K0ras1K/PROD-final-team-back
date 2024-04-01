package ru.droptableusers.sampleapi.tasks

import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.GetUpdates
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.GetUpdatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.GroupModel
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.handler.TextHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.ProfileHandler

object TelegramUpdateHandler {
    var updateId: Int = 0

    fun handle(updates: List<Update>) {
        for (update in updates) {
            println(update)
            try {
                val callbackQuery: CallbackQuery? = update.callbackQuery()
                val message: Message? = update.message()
                if (message != null) {
                    println(message.text())
                    TextHandler(message).handle()
                }
                if (callbackQuery != null) {
                    if (callbackQuery!!.data().startsWith("verif-")) {
                        val userId: Int = callbackQuery.data().split("-")[1].toInt()
                        println("Кнопка бана сработала!")
                        val groupData = GroupPersistence().select(userId)!!
                        val userModel = UserPersistence().selectById(userId)!!
                        if (groupData.group == Group.NOT_VERIFIED) {
                            GroupPersistence().update(
                                GroupModel(
                                    id = groupData.id,
                                    group = Group.MEMBER,
                                ),
                            )
                            TelegramChat.VERIFICATION.BOT.execute(
                                SendMessage(
                                    TelegramChat.VERIFICATION.CHAT_ID,
                                    "Пользователь ${userModel.firstName} ${userModel.lastName} верифицирован",
                                ),
                            )
                        }
                    }
                    if (callbackQuery.data().startsWith("show-teams")) {
                    }
                    if (callbackQuery.data() == "show-profile") {
                        ProfileHandler(callbackQuery).handle()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startGettingUpdates() {
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                for (chat in TelegramChat.values()) {
                    println("Get updates from chat $chat")
                    val updatesResponse: GetUpdatesResponse =
                        chat.BOT.execute(
                            GetUpdates().limit(100).offset(updateId + 1),
                        )
                    handle(updatesResponse.updates())
                }

                delay(10_000)
            }
        }
    }

    fun startGetUpdates() {
        for (bot in TelegramChat.values()) {
            bot.BOT.setUpdatesListener(
                { // process updates
                    handle(it)
                    UpdatesListener.CONFIRMED_UPDATES_ALL
                }, // Create Exception Handler
                { e ->
                    if (e.response() != null) {
                        // got bad response from telegram
                        e.response().errorCode()
                        e.response().description()
                    } else {
                        // probably network error
                        e.printStackTrace()
                    }
                },
            )
        }
    }
}
