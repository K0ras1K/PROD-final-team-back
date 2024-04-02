package ru.droptableusers.sampleapi.telegram.handler.callback.teams

import com.pengrad.telegrambot.model.CallbackQuery
import ru.droptableusers.sampleapi.telegram.handler.AbstractCallbackQueryHandler

class MyTeamHandler(val callbackQuery: CallbackQuery) : AbstractCallbackQueryHandler(callbackQuery)
