package ru.droptableusers.sampleapi.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

abstract class AbstractController(val call: ApplicationCall) {
    val principal = call.principal<JWTPrincipal>()
    val login = principal!!.payload.getClaim("username").asString()
}
