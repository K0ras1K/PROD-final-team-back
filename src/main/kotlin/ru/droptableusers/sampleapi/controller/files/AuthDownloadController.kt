package ru.droptableusers.sampleapi.controller.files

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import java.io.File

class AuthDownloadController(call: ApplicationCall) : AbstractController(call) {
    suspend fun download() {
        val fileName = call.parameters["file_name"]!!
        val requiredUserId = fileName.split(",")[0].toInt()
        if (requiredUserId == id!! || userGroup.group.ordinal < 2) {
            val file = File("public/$fileName")
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
            }
        } else {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("Forbidden"))
        }
    }
}