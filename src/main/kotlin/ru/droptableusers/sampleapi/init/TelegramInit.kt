package ru.droptableusers.sampleapi.init

import ru.droptableusers.sampleapi.tasks.TelegramUpdateHandler

class TelegramInit {
    fun initialize() {
        TelegramUpdateHandler.startGetUpdates()
    }
}
