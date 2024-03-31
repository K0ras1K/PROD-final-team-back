package ru.droptableusers.sampleapi.tasks

import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.ChosenInlineResult
import com.pengrad.telegrambot.model.InlineQuery
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.GetUpdates
import com.pengrad.telegrambot.response.GetUpdatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.base.GroupModel
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence

object TelegramUpdateHandler {
    var updateId: Int = 0

    fun handle(updates: List<Update>) {
        println("Started handling updates")
        for (update in updates) {
            println(update.toString())
            try {
                val inlineQuery: InlineQuery? = update.inlineQuery()
                val chosenInlineResult: ChosenInlineResult? = update.chosenInlineResult()
                val callbackQuery: CallbackQuery? = update.callbackQuery() ?: return
                if (callbackQuery!!.data().startsWith("verif-")) {
                    val userId: Int = callbackQuery.data().split("-")[1].toInt()
                    println("Кнопка бана сработала!")
                    val groupData = GroupPersistence().select(userId)!!
                    if (groupData.group == Group.NOT_VERIFIED) {
                        GroupPersistence().update(
                            GroupModel(
                                id = groupData.id,
                                group = Group.MEMBER,
                            ),
                        )
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
