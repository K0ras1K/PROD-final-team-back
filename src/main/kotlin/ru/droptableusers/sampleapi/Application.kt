package ru.droptableusers.sampleapi

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import ru.droptableusers.sampleapi.database.DatabaseFactory
import ru.droptableusers.sampleapi.init.Initialization
import ru.droptableusers.sampleapi.plugins.configureJWT
import ru.droptableusers.sampleapi.plugins.configureRouting
import ru.droptableusers.sampleapi.plugins.configureSerialization
import ru.droptableusers.sampleapi.plugins.configureSwagger
import ru.droptableusers.sampleapi.utils.Logger

/**
 * Main
 *
 * @author Roman K0ras1K Kalmykov
 */
fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

/**
 * Module
 *
 * @author Roman K0ras1K Kalmykov
 */
fun Application.module() {
    while (true) {
        try {
            connect()
            break
        } catch (exception: Exception) {
            Logger.logger.error("Нет подключения в БД")
        }
    }
    Initialization.init()
    configureSerialization()
    configureJWT()
    configureSwagger()
    configureRouting()
    Logger.logger.info("Successfully started api")
}

/**
 * Connect
 *
 * @author Roman K0ras1K Kalmykov
 */
fun connect() {
    val db =
        Database.connect(
            DatabaseFactory.createHikariDataSource(
                "jdbc:postgresql://postgres/sampleapi",
                "org.postgresql.Driver",
                "K0ras1K",
                "Shah!9Sah@",
            ),
        )
}
