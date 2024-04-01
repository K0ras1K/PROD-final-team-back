package ru.droptableusers.sampleapi.controller.users

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.pengrad.telegrambot.request.SendMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt
import ru.droptableusers.sampleapi.ApplicationConstants
import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.data.enums.TelegramChat
import ru.droptableusers.sampleapi.data.enums.ValidationStatus
import ru.droptableusers.sampleapi.data.models.base.GroupModel
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.LoginInputModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.RegisterInputModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.TokenRespondOutput
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import ru.droptableusers.sampleapi.database.persistence.ValidateDataPersistence
import ru.droptableusers.sampleapi.tasks.Keyboard
import ru.droptableusers.sampleapi.utils.DateUtils
import ru.droptableusers.sampleapi.utils.Validation
import java.util.*

class UsersController(val call: ApplicationCall) {
    suspend fun register() {
        runBlocking {
            val receive = call.receive<RegisterInputModel>()
            println("after receive")

            val validations = mutableMapOf<String, ValidationStatus>()
            println(receive)

            println("before validation")
            validations["username"] =
                Validation.validateField(receive.username, 256, "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}\$")
            validations["password"] = Validation.validatePassword(receive.password)

            for (field in validations) {
                if (field.value != ValidationStatus.ACCEPTED) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("${field.value} Поле: ${field.key}"))
                    return@runBlocking
                }
            }
            println("after validation")
            println(validations)

            if (UserPersistence().selectByUsername(receive.username) != null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse("Пользователь с таким логином уже зарегистрирован!"),
                )
                return@runBlocking
            }

            println("before hash")
            val passwordHash = BCrypt.hashpw(receive.password, BCrypt.gensalt())
            val targetUserData =
                UserModel(
                    username = receive.username,
                    password = passwordHash,
                    regTime = System.currentTimeMillis(),
                    tgLogin = receive.tgLogin,
                    firstName = receive.firstName,
                    lastName = receive.lastName,
                    birthdayDate = receive.birthdayDate,
                    id = 0,
                    description = "",
                    major = receive.major,
                )
            val userModel = UserPersistence().insert(targetUserData)
            if (
                !ValidateDataPersistence().validate(
                    firstName = userModel!!.firstName,
                    lastName = userModel.lastName,
                    birthdayDate = userModel.birthdayDate,
                    email = userModel.username,
                )
            ) {
                GroupPersistence().insert(
                    GroupModel(
                        id = userModel!!.id,
                        group = Group.NOT_VERIFIED,
                    ),
                )
                TelegramChat.VERIFICATION.BOT.execute(
                    SendMessage(
                        TelegramChat.VERIFICATION.CHAT_ID,
                        """
                        Неизвестный пользователь.
                        ${userModel.firstName} ${userModel.lastName}
                        Дата рождения: ${DateUtils.getCurrentDateAsString(userModel.birthdayDate)}
                        Почта: ${userModel.username}
                        """.trimIndent(),
                    ).replyMarkup(Keyboard.generateVerificationKeyboard(userModel.id)),
                )
            } else {
                GroupPersistence().insert(
                    GroupModel(
                        id = userModel!!.id,
                        group = Group.MEMBER,
                    ),
                )
            }
            println("after insert")

            val token =
                JWT.create()
                    .withClaim("username", targetUserData.username)
                    .withClaim("passwordHash", targetUserData.password)
                    .withClaim("id", targetUserData.id)
                    .withExpiresAt(Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 14L))
                    .sign(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN))
            call.respond(HttpStatusCode.Created, TokenRespondOutput(token))
        }
    }

    suspend fun login() {
        runBlocking {
            val receive = call.receive<LoginInputModel>()

            if (UserPersistence().selectByUsername(receive.username) == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("Пользователь с указанным логином и паролем не найден"),
                )
                return@runBlocking
            }

            val selectedUser = UserPersistence().selectByUsername(receive.username)!!

            if (!BCrypt.checkpw(receive.password, selectedUser.password)) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("Пользователь с указанным логином и паролем не найден"),
                )
                return@runBlocking
            }

            val token =
                JWT.create()
                    .withClaim("username", selectedUser.username)
                    .withClaim("passwordHash", selectedUser.password)
                    .withClaim("id", selectedUser.id)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 60 * 24 * 60))
                    .sign(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN))
            call.respond(TokenRespondOutput(token))
        }
    }
}
