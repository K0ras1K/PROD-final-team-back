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
import ru.droptableusers.sampleapi.telegram.handler.FilesHandler
import ru.droptableusers.sampleapi.telegram.handler.TextHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.MainHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.ProfileHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.documents.ShowDocumentsHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.documents.ShowFullDocumentHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.invites.ShowAllInvites
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.AcceptTeamHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.ApplyTeamHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.FullTeamHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.teams.TeamsHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.users.AcceptUserHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.users.ApplyUserHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.users.FullUserHandler
import ru.droptableusers.sampleapi.telegram.handler.callback.users.UsersWithoutTeamHandler

object TelegramUpdateHandler {
    var updateId: Int = 0

    fun handle(updates: List<Update>) {
        for (update in updates) {
            println(update)
            try {
                val callbackQuery: CallbackQuery? = update.callbackQuery()
                val message: Message? = update.message()
                if (message != null) {
                    if (message.text() != null) {
                        println(message.text())
                        TextHandler(message).handle()
                    }
                    if (message.document() != null) {
                        FilesHandler(message).handle()
                    }
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
                    if (callbackQuery.data() == "main") {
                        MainHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-teams")) {
                        TeamsHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-users")){
                        UsersWithoutTeamHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-user-")){
                        FullUserHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data() == "show-profile") {
                        ProfileHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-team-")) {
                        FullTeamHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("apply-team-")) {
                        ApplyTeamHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("apply-user-")) {
                        ApplyUserHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("accept-team-")) {
                        AcceptTeamHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("accept-user-")) {
                        AcceptUserHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-documents")) {
                        ShowDocumentsHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-doc-")) {
                        ShowFullDocumentHandler(callbackQuery).handle()
                    }
                    if (callbackQuery.data().startsWith("show-all-invites")) {
                        ShowAllInvites(callbackQuery).handle()
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
