package ru.droptableusers.sampleapi.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence

abstract class AbstractController(val call: ApplicationCall) {
    val principal = call.principal<JWTPrincipal>()
    val login = principal!!.payload.getClaim("username").asString()
    val userGroup = GroupPersistence().select(principal!!.payload.getClaim("id").asInt())!!
    val id = principal!!.payload.getClaim("id").asInt()
}
