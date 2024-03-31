package ru.droptableusers.sampleapi.init

/**
 * Initialization
 *
 * @constructor Create empty Initialization
 * @author Roman K0ras1K Kalmykov
 */
object Initialization {
    fun init() {
        DatabaseInit().initialize()
    }
}
