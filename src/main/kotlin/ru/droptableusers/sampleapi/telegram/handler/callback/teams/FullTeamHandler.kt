package ru.droptableusers.sampleapi.telegram.handler.callback.teams

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.EditMessageText
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.models.inout.output.users.ProfileOutputResponse
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.telegram.keyboard.FullTeamKeyboard
import ru.droptableusers.sampleapi.telegram.models.base.FullTeamBotModel

class FullTeamHandler(val callbackQuery: CallbackQuery) {
    fun handle() {
        val teamId = callbackQuery.data().split("-")[2].toInt()
        val teamData = TeamsPersistence().selectById(teamId)!!

        val teamMembers =
            UserPersistence().selectByIdList(
                TeamsPersistence().selectTeammates(teamId),
            ).map {
                ProfileOutputResponse(
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    tgLogin = it.tgLogin,
                    registerAt = it.regTime,
                    group = GroupPersistence().select(it.id)!!.group,
                    id = it.id,
                    description = it.description,
                    team = teamId,
                    major = it.major,
                    tags = TagsPersistence().getTagsByIdList(UserPersistence().selectTagIds(it.id))
                )
            }

        val model =
            FullTeamBotModel(
                teamId,
                teamMembers,
            )

        TelegramChat.VERIFICATION.BOT.execute(
            EditMessageText(
                callbackQuery.message().chat().id(),
                callbackQuery.message().messageId(),
                """
                Команда ${teamData.name}
                Описание: ${teamData.description}
                """.trimIndent(),
            ).replyMarkup(FullTeamKeyboard.generateFullTeamKeyboard(model)).parseMode(ParseMode.MarkdownV2),
        )
    }
}
