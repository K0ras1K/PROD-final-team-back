package ru.droptableusers.sampleapi.data.enums

import com.pengrad.telegrambot.TelegramBot

enum class TelegramChat(val BOT: TelegramBot, val CHAT_ID: String) {
    VERIFICATION(TelegramBot("7063283346:AAGIqmIyKxRQPDWs3dQIruXIosQcnOHAexs"), "-4157148487"),
}
