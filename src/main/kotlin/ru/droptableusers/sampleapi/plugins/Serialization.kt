package ru.droptableusers.sampleapi.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

/**
 * Configure serialization
 *
 * @author Roman K0ras1K Kalmykov
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = false
            },
        )
    }
}
