package ru.droptableusers.sampleapi.controller.files

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.database.schema.UserTable.username
import java.io.File
import java.util.*

class AuthUploadController(call: ApplicationCall) : AbstractController(call) {
    val publicPath: String = "public"

    val filePath: String = "$publicPath/files/"
    val bannerPath: String = "$publicPath/banners/"
    val iconPath: String = "$publicPath/icons/"
    val avatarPath: String = "$publicPath/avatars/"

    suspend fun uploadPdfFile() {
        val multipartData = call.receiveMultipart()
        var fileDescription = ""
        var fileName = ""
        val fileUUID = UUID.randomUUID()

        multipartData.forEachPart { part ->

            when (part) {
                is PartData.FormItem -> {
                    fileDescription = part.value
                }

                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    val fileBytes = part.streamProvider().readBytes()
                    val createdfile = File("$filePath/$fileUUID.pdf").createNewFile()
                    val file = File("$filePath$username.pdf")
                    file.writeBytes(fileBytes)
                }

                else -> {}
            }
            part.dispose()
        }
        call.respond(HttpStatusCode.OK, fileUUID)
    }
}
