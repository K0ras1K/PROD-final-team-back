package ru.droptableusers.sampleapi.controller.files

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.io.File

class PublicDownloadController(val call: ApplicationCall) {
    suspend fun download() {
        // get filename from request url
        val fileName = call.parameters["file_name"]!!
        val folderName = call.parameters["folder_name"]!!
        // construct reference to file
        // ideally this would use a different filename
        val file = File("public/$folderName/$fileName")
        if (file.exists()) {
            call.respondFile(file)
        } else {
            when (folderName) {
                "banners" -> call.respondFile(File("public/avatars/default.png"))
                "icons" -> call.respondFile(File("public/skins/default.png"))
                else -> call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
