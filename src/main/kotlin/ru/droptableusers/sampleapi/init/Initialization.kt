package ru.droptableusers.sampleapi.init

import ru.droptableusers.sampleapi.utils.Logger

/**
 * Initialization
 *
 * @constructor Create empty Initialization
 * @author Roman K0ras1K Kalmykov
 */
object Initialization {
    fun init() {
        Logger.logger.info("Starting initializing database")
        DatabaseInit().initialize()
        Logger.logger.info("Database successfully initialized")
        // -------------------------------------------------
        Logger.logger.info("Starting initializing telegram")
        TelegramInit().initialize()
        Logger.logger.info("Telegram successfully initialized")
    }
}
