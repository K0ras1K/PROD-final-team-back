package ru.droptableusers.sampleapi.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "swagger-ui"
            forwardRoot = true
        }
        info {
            title = "DropTableUsers API"
            version = "0.1"
            description = "Api for PROD final"
        }
        server {
            url = "https://api.ethereally.space"
            description = "Development Server"
        }
    }
}
